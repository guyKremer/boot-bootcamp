package jettyServer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import jettyServer.configuration.ServerConfiguration;
import org.apache.http.HttpHost;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.File;

public class ServerModule extends AbstractModule {

    private ObjectMapper objectMapper;

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
    public ServerConfiguration provideServerConfiguration() {
        ServerConfiguration serverConfiguration;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        try {
            serverConfiguration = mapper.readValue(new File("/usr/server.config"), ServerConfiguration.class);
            return serverConfiguration;
        }
        catch (Exception e ){
            throw new RuntimeException(e);
        }
    }

    @Provides
    RestHighLevelClient provideRestHighLevelClient(){
        ServerConfiguration serverConfiguration = provideServerConfiguration();
        return new RestHighLevelClient(RestClient.builder(
                  new HttpHost(serverConfiguration.getElasticSearchHost(),serverConfiguration.getGetElasticSearchPort(), "http")));
    }
}
