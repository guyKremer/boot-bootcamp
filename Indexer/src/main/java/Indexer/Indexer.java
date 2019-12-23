package Indexer;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import com.google.inject.Inject;
import configuration.IndexerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;

import javax.ws.rs.core.Response;


public class Indexer {

    IndexerConfiguration indexerConfiguration;
    KafkaConsumer consumer;
    RestHighLevelClient elasticClient;

    @Inject
    public Indexer(KafkaConsumer consumer, RestHighLevelClient restHighLevelClient, IndexerConfiguration indexerConfiguration){
        this.indexerConfiguration = indexerConfiguration;
        this.consumer = consumer;
        this.elasticClient = restHighLevelClient;
    }

    public void run(){

        try{
            while (true) {
                ConsumerRecords<String, Map> records = consumer.poll(Duration.ofMillis(indexerConfiguration.getIntervalForPoll()));
                if(!records.isEmpty()){
                    index(records);
                    consumer.commitSync();
                }
            }
        }
        finally {
            consumer.close();
        }
    }
    private BulkResponse index(ConsumerRecords<String, Map> records) {

        String index = "bootacmp";
        String logType="_doc";
        BulkRequest elasticBulkRequest = new BulkRequest();

        try {
            for (ConsumerRecord<String,Map> record : records){
                elasticBulkRequest.add(new IndexRequest(index, logType).source(record.value()));
            }
            BulkResponse elasticBulkResponse = elasticClient.bulk(elasticBulkRequest);
            return elasticBulkResponse;
        }
        catch (IOException e){
            throw new RuntimeException(e.getCause());
        }
    }
}
