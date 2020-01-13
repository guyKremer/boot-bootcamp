package Indexer;

import java.time.Duration;
import static java.util.Objects.requireNonNull;

import accounts.AccountsClient;
import com.google.inject.Inject;
import configuration.IndexerConfiguration;
import kafka.KafkaRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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
    private static final String LOG_TYPE = "_doc";

    @Inject
    public Indexer(KafkaConsumer consumer, RestHighLevelClient restHighLevelClient, IndexerConfiguration indexerConfiguration, AccountsClient accountsClient){
        this.indexerConfiguration = requireNonNull(indexerConfiguration);
        this.consumer = requireNonNull(consumer);
        this.elasticClient = requireNonNull(restHighLevelClient);
        this.accountsClient = requireNonNull(accountsClient);
    }

    public void run(){

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

    private BulkResponse index(ConsumerRecords<String, String> records) {
        ObjectMapper mapper = new ObjectMapper();
        BulkRequest elasticBulkRequest = new BulkRequest();

        try {
            for (ConsumerRecord<String, String> record : records){
                KafkaRecord kafkaRecord = mapper.readValue(record.value(),KafkaRecord.class);
                String accountIndex = accountsClient.getAccount(kafkaRecord.getAccountToken()).getIndexName();
                elasticBulkRequest.add(new IndexRequest(accountIndex, LOG_TYPE).source(kafkaRecord.getSource()));
            }
            BulkResponse elasticBulkResponse = elasticClient.bulk(elasticBulkRequest);
            return elasticBulkResponse;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
