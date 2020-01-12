package jettyServer.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jettyServer.configuration.QueryResourceConfiguration;
import jettyServer.resources.queryResource.QueryResource;
import org.apache.http.HttpHost;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.File;

public class QueryResourceModule extends AbstractModule {


    private QueryResourceConfiguration queryResourceConfiguration;

    public QueryResourceModule(QueryResourceConfiguration queryResourceConfiguration) {
        this.queryResourceConfiguration = queryResourceConfiguration;
    }

    @Override
    protected void configure() {

    }

    @Provides
    public RestHighLevelClient provideRestHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(queryResourceConfiguration.getElasticSearchHost(), queryResourceConfiguration.getElasticSearchPort(), "http")));
    }

}
