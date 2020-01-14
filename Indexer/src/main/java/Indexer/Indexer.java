package Indexer;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import com.google.inject.Inject;
import configuration.IndexerConfiguration;
import exceptions.DbAccessException;
import kafka.KafkaRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;


public class Indexer {

    private final IndexerConfiguration indexerConfiguration;
    private final KafkaConsumer consumer;
    private final RestHighLevelClient elasticClient;
    private final AccountsClient accountsClient;
    private final Logger logger = LogManager.getLogger(Indexer.class);
    private static final String LOG_TYPE = "_doc";


    @Inject
    public Indexer(KafkaConsumer consumer, RestHighLevelClient restHighLevelClient, IndexerConfiguration indexerConfiguration, AccountsClient accountsClient) {
        this.indexerConfiguration = requireNonNull(indexerConfiguration);
        this.consumer = requireNonNull(consumer);
        this.elasticClient = requireNonNull(restHighLevelClient);
        this.accountsClient = requireNonNull(accountsClient);
    }

    public void run() {

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(indexerConfiguration.getIntervalForPoll()));
                if (!records.isEmpty()) {
                    index(records);
                    consumer.commitAsync();
                }
            }
        } finally {
            consumer.close();
        }
    }

    private void index(ConsumerRecords<String, String> records) {
        ObjectMapper mapper = new ObjectMapper();
        BulkRequest elasticBulkRequest = new BulkRequest();

        for (ConsumerRecord<String, String> record : records) {
            try{
                KafkaRecord kafkaRecord = mapper.readValue(record.value(), KafkaRecord.class);
                Optional<AccountData> OptionalAccountData = accountsClient.getAccount(kafkaRecord.getAccountToken());
                AccountData accountData = OptionalAccountData.get();
                String accountIndex = accountData.getIndexName();
                elasticBulkRequest.add(new IndexRequest(accountIndex, LOG_TYPE).source(kafkaRecord.getSource()));
            }
            catch (IOException | DbAccessException e){
                logger.debug("couldn't index " + record.value());
            }
        }
        try{
            BulkResponse elasticBulkResponse = elasticClient.bulk(elasticBulkRequest);
        }
        catch (IOException ioe){
            StringBuilder recordsAsString = new StringBuilder();
            for (ConsumerRecord<String, String> record : records){
                recordsAsString.append(record.value()).append('\n');
            }
            logger.debug("couldn't index the following documents:\n" + recordsAsString);
        }
    }
}
