package Indexer;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import configuration.IndexerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;

public class Indexer {

    private final IndexerConfiguration indexerConfiguration;
    private final KafkaConsumer consumer;
    private final RestHighLevelClient elasticClient;
    private static final String index = "bootacmp";
    private static final String logType = "_doc";

    @Inject
    public Indexer(KafkaConsumer consumer, RestHighLevelClient restHighLevelClient, IndexerConfiguration indexerConfiguration) {

        this.consumer = requireNonNull(consumer);
        this.indexerConfiguration = requireNonNull(indexerConfiguration);
        this.elasticClient = requireNonNull(restHighLevelClient);
    }

    public void run() {

        try {
            while (true) {
                ConsumerRecords<String, Map> records = consumer.poll(Duration.ofMillis(indexerConfiguration.getIntervalForPoll()));
                if (!records.isEmpty()) {
                    index(records);
                    consumer.commitAsync();
                }
            }
        } finally {
            consumer.close();
        }
    }

    private BulkResponse index(ConsumerRecords<String, Map> records) {

        BulkRequest elasticBulkRequest = new BulkRequest();

        try {
            for (ConsumerRecord<String, Map> record : records) {
                elasticBulkRequest.add(new IndexRequest(index, logType).source(record.value()));
            }
            BulkResponse elasticBulkResponse = elasticClient.bulk(elasticBulkRequest);
            return elasticBulkResponse;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
