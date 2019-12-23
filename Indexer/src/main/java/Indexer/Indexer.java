package Indexer;

import java.time.Duration;
import java.util.Map;

import com.google.inject.Inject;
import configuration.IndexerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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

        while (true) {
            ConsumerRecords<String, Map> records = consumer.poll(Duration.ofMillis(indexerConfiguration.getIntervalForPoll()));
            boolean indexingSucceeded = index(records);

            if(indexingSucceeded){
                consumer.commitSync();
            }
        }
    }

    // Index a batch of records.
    // returns false if one of the records failed to index.
    private boolean index(ConsumerRecords<String, Map> records) {

        boolean res = true;

        try{
            for (ConsumerRecord<String,Map> record : records){
                boolean indexingSucceeded = index(record);
                if( ! indexingSucceeded){
                    res = false;
                    break;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getCause());
            throw new RuntimeException(e);
        }

        return res;
    }

    // Index a single record.
    // returns false if the record failed to index.
    private boolean index(ConsumerRecord<String,Map> record){


        String index = "bootacmpppp";
        String logType="_doc";

        try {
            IndexResponse response = elasticClient.index(new IndexRequest(index, logType).source(record.value()));
            if(response.status().getStatus() == Response.Status.CREATED.getStatusCode()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e.getCause());
            throw new RuntimeException(e);
        }

    }
}
