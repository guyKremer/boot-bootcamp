package jettyServer.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import jettyServer.configuration.IndexingResourceConfiguration;
import jettyServer.configuration.QueryResourceConfiguration;
import jettyServer.configuration.ServerConfiguration;

import java.io.File;
import java.io.IOException;

public class ServerModule extends AbstractModule {

    private final ServerConfiguration serverConfiguration;

    public ServerModule(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    @Override
    protected void configure() {
        ObjectMapper mapper = new ObjectMapper();
        binder().requireExplicitBindings();
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("jettyServer.resources")
                .addPort(serverConfiguration.getPort())
                .withContextPath("/*")
                .build();
        try {
            IndexingResourceConfiguration indexingResourceConfiguration = mapper.readValue(new File("/usr/indexingResource.config"), IndexingResourceConfiguration.class);
            QueryResourceConfiguration queryResourceConfiguration = mapper.readValue(new File("/usr/queryResource.config"), QueryResourceConfiguration.class);
            install(new JerseyModule(configuration));
            install(new QueryResourceModule(queryResourceConfiguration));
            install(new IndexingResourceModule(indexingResourceConfiguration));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

}
