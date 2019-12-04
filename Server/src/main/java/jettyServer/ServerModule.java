package jettyServer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import jettyServer.configuration.ServerConfiguration;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;

public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("jettyServer")
                .addPort(8001)
                .withContextPath("/*")
                .build();

        install(new JerseyModule(configuration));
    }

    @Provides
    public ServerConfiguration provideServerConfiguration() {
        ServerConfiguration serverConfiguration = new ServerConfiguration();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        try {
            serverConfiguration = mapper.readValue(new File("./configurations/server.config"), ServerConfiguration.class);
        }
        catch (Exception e ){

        }
        return serverConfiguration;
    }
}
