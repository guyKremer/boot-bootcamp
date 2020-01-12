package jettyServer.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jettyServer.configuration.IndexingResourceConfiguration;
import jettyServer.configuration.QueryResourceConfiguration;
import jettyServer.configuration.ServerConfiguration;
import kafka.ObjectSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class IndexingResourceModule extends AbstractModule {

    private IndexingResourceConfiguration indexingResourceConfiguration;

    public IndexingResourceModule(IndexingResourceConfiguration indexingResourceConfiguration) {
        this.indexingResourceConfiguration = indexingResourceConfiguration;
    }

    @Override
    protected void configure() {
        bind(IndexingResourceConfiguration.class).toInstance(indexingResourceConfiguration);
    }


    @Provides
    public Producer provideKafkaProducer() {

        String brokerHost = indexingResourceConfiguration.getBrokerHost();
        int brokerPort = indexingResourceConfiguration.getBrokerPort();
        String kafkaUri = brokerHost + ":" + brokerPort;

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaUri);
        props.put("acks", "all");
        props.put("key.serializer", ObjectSerializer.class.getName());
        props.put("value.serializer", ObjectSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}
