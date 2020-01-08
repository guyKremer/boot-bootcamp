package di;

import accounts.AccountsClient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import configuration.IndexerConfiguration;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.codehaus.jackson.map.ObjectMapper;
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

    }

    @Provides
    public IndexerConfiguration providesIndexerConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("../usr/indexer.config"), IndexerConfiguration.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    public KafkaConsumer providesConsumer(Properties props, IndexerConfiguration indexerConfiguration){
            KafkaConsumer<String, Map> consumer =  new KafkaConsumer<>(props);

            consumer.subscribe(Arrays.asList(indexerConfiguration.getTopic()));
            return consumer;
    }

    @Provides
    public Properties provideProperties(IndexerConfiguration indexerConfiguration){
        Properties props = new Properties();
        String kafkaUri = indexerConfiguration.getBrokerHost() + ":" + indexerConfiguration.getBrokerPort();
        props.setProperty("bootstrap.servers", kafkaUri);
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.commit.interval.ms", Long.toString(indexerConfiguration.getIntervalForCommit()));
        props.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    @Provides
    RestHighLevelClient provideRestHighLevelClient(IndexerConfiguration indexerConfiguration){
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(indexerConfiguration.getElasticSearchHost(),indexerConfiguration.getElasticSearchPort(), "http")));
    }

    @Provides
    AccountsClient providesAccountsClient(IndexerConfiguration indexerConfiguration){
        return new AccountsClient(indexerConfiguration.getAccountsClientHost());
    }
}
