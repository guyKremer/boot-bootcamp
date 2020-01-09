package di;

import Indexer.Indexer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import configuration.IndexerConfiguration;
import kafka.StreamToMapDeserializer;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class IndexerModule extends AbstractModule {

    public IndexerModule() {
    }

    @Override
    protected void configure() {
        bind(Indexer.class);
    }


    @Provides
    public IndexerConfiguration providesIndexerConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("../usr/indexer.config"), IndexerConfiguration.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    public KafkaConsumer providesConsumer(Properties props, IndexerConfiguration indexerConfiguration) {
        KafkaConsumer<String, Map> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList(indexerConfiguration.getTopic()));
        return consumer;
    }

    @Provides
    public Properties provideProperties(IndexerConfiguration indexerConfiguration) {
        Properties props = new Properties();
        String kafkaUri = indexerConfiguration.getBrokerHost() + ":" + indexerConfiguration.getBrokerPort();
        props.setProperty("bootstrap.servers", kafkaUri);
        props.setProperty("group.id", indexerConfiguration.getGroupId());
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.commit.interval.ms", Long.toString(indexerConfiguration.getIntervalForCommit()));
        props.setProperty("key.deserializer", StreamToMapDeserializer.class.getName());
        props.setProperty("value.deserializer", StreamToMapDeserializer.class.getName());
        return props;
    }

    @Provides
    RestHighLevelClient provideRestHighLevelClient(IndexerConfiguration indexerConfiguration) {
        System.out.println(indexerConfiguration.getElasticSearchHost());
        System.out.println(indexerConfiguration.getElasticSearchPort());

        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(indexerConfiguration.getElasticSearchHost(), indexerConfiguration.getElasticSearchPort(), "http")));
    }
}
