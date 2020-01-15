package Indexer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import com.google.inject.Inject;
import common.parsers.JsonParser;
import configuration.IndexerConfiguration;
import kafka.KafkaRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;


public class Indexer {

    private final IndexerConfiguration indexerConfiguration;
    private final KafkaConsumer consumer;
    private final RestHighLevelClient elasticClient;
    private final AccountsClient accountsClient;
    private final Logger logger = LogManager.getLogger(Indexer.class);
    private static final String LOG_TYPE = "_doc";
    private List<ConsumerRecord<String, String>> failedToIndexRecords = new ArrayList<>();


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
        BulkRequest elasticBulkRequest = new BulkRequest();

        for (ConsumerRecord<String, String> record : records) {
            try {
                KafkaRecord kafkaRecord = JsonParser.parse(record.value(), KafkaRecord.class);
                Optional<AccountData> optionalAccountData = accountsClient.getAccount(kafkaRecord.getAccountToken());
                if(optionalAccountData.isPresent()){
                    AccountData accountData = optionalAccountData.get();
                    String accountIndex = accountData.getIndexName();
                    elasticBulkRequest.add(new IndexRequest(accountIndex, LOG_TYPE).source(kafkaRecord.getSource()));
                }
            } catch (Exception e) {
                failedToIndexRecords.add(record);
            }
        }
        try {
            elasticClient.bulk(elasticBulkRequest);
        } catch (IOException ioe) {
            for (ConsumerRecord<String, String> record : records) {
                failedToIndexRecords.add(record);
            }
        }

        if (!failedToIndexRecords.isEmpty()) {
            logFailedRecords(failedToIndexRecords);
        }
    }

    private void logFailedRecords(List<ConsumerRecord<String, String>> failedToIndexRecords){
        StringBuilder recordsAsString = new StringBuilder();
        for (ConsumerRecord<String, String> record : failedToIndexRecords) {
            recordsAsString.append(record.value());
        }
        logger.debug("couldn't index the following documents:\n" + recordsAsString);
    }
}
