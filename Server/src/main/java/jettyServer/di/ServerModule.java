package jettyServer.di;

import accounts.AccountsClient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import common.parsers.JsonParser;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import jettyServer.configuration.IndexingResourceConfiguration;
import jettyServer.configuration.QueryResourceConfiguration;
import jettyServer.configuration.ServerConfiguration;

import java.io.File;

public class ServerModule extends AbstractModule {

    private final ServerConfiguration serverConfiguration;

    public ServerModule(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("jettyServer.resources")
                .addPort(serverConfiguration.getPort())
                .withContextPath("/*")
                .build();
        IndexingResourceConfiguration indexingResourceConfiguration = JsonParser.parse(new File("/usr/indexingResource.config"), IndexingResourceConfiguration.class);
        QueryResourceConfiguration queryResourceConfiguration = JsonParser.parse(new File("/usr/queryResource.config"), QueryResourceConfiguration.class);
        install(new JerseyModule(configuration));
        install(new QueryResourceModule(queryResourceConfiguration));
        install(new IndexingResourceModule(indexingResourceConfiguration));
    }

    @Provides
    AccountsClient providesAccountsClient() {
        return new AccountsClient(serverConfiguration.getAccountsManagerBaseUri());
    }

}
