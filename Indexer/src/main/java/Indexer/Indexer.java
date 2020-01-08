package Indexer;

import java.io.IOException;
import java.time.Duration;

import accounts.AccountsClient;
import accounts.exceptions.OperationFailedException;
import accounts.pojos.AccountData;
import com.google.inject.Inject;
import configuration.IndexerConfiguration;
import kafka.KafkaRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;


public class Indexer {

    IndexerConfiguration indexerConfiguration;
    KafkaConsumer consumer;
    RestHighLevelClient elasticClient;
    AccountsClient accountsClient;

    @Inject
    public Indexer(KafkaConsumer consumer, RestHighLevelClient restHighLevelClient, IndexerConfiguration indexerConfiguration,AccountsClient accountsClient){
        this.indexerConfiguration = indexerConfiguration;
        this.consumer = consumer;
        this.elasticClient = restHighLevelClient;
        this.accountsClient = accountsClient;
    }

    public void run(){

        try{
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(indexerConfiguration.getIntervalForPoll()));
                if(!records.isEmpty()){
                    BulkResponse bulkItemResponses = index(records);
                    consumer.commitSync();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            consumer.close();
        }
    }
    private BulkResponse index(ConsumerRecords<String, String> records) {

        String logType="_doc";
        ObjectMapper mapper = new ObjectMapper();
        BulkRequest elasticBulkRequest = new BulkRequest();

        try {
            for (ConsumerRecord<String, String> record : records){
                KafkaRecord kafkaRecord = mapper.readValue(record.value(),KafkaRecord.class);
                String accountIndex = accountsClient.getAccount(kafkaRecord.getAccountToken()).getEsIndexName();
                elasticBulkRequest.add(new IndexRequest(accountIndex, logType).source(kafkaRecord.getSource()));
            }
            BulkResponse elasticBulkResponse = elasticClient.bulk(elasticBulkRequest);
            return elasticBulkResponse;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}