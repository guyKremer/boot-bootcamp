package jettyServer;

import accounts.AccountsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import jettyServer.configuration.ServerConfiguration;
import org.apache.http.HttpHost;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.File;
import java.util.Properties;

public class ServerModule extends AbstractModule {

    public ServerModule(){

    }

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("jettyServer.resources")
                .addPort(8001)
                .withContextPath("/*")
                .build();

        install(new JerseyModule(configuration));

    }

    @Provides
    @Singleton
    public ServerConfiguration provideServerConfiguration() {
        ServerConfiguration serverConfiguration;
        ObjectMapper mapper = new ObjectMapper();
        try {
            serverConfiguration = mapper.readValue(new File("/usr/server.config"), ServerConfiguration.class);
            return serverConfiguration;
        }
        catch (Exception e ){
            throw new RuntimeException(e);
        }
    }

    @Provides
    public RestHighLevelClient provideRestHighLevelClient(){
        ServerConfiguration serverConfiguration = provideServerConfiguration();
        return new RestHighLevelClient(RestClient.builder(
                  new HttpHost(serverConfiguration.getElasticSearchHost(),serverConfiguration.getGetElasticSearchPort(), "http")));
    }
    @Provides
    public Producer provideKafkaProducer(){

        String brokerHost = "kafkabroker";
        int brokerPort = 9092;
        String kafkaUri = brokerHost+":"+brokerPort;

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaUri);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    @Provides
    AccountsClient providesAccountsClient(ServerConfiguration serverConfiguration){
        return new AccountsClient(serverConfiguration.getAccountsClientHost());
    }

}
