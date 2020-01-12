package di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import configuration.MybatisConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

import java.io.File;
import java.io.IOException;

public class AccountsManagerModule extends AbstractModule {

    public AccountsManagerModule(){

    }

    @Override
    protected void configure() {
        ObjectMapper mapper = new ObjectMapper();
        binder().requireExplicitBindings();
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("jettyServer.resources")
                .addPort(80)
                .withContextPath("/*")
                .build();

        bind(DefaultObjectWrapperFactory.class);
        bind(DefaultObjectFactory.class);
        install(new JerseyModule(configuration));
        try {
            install(new MyBatisAccountsModule(mapper.readValue(new File("/usr/myBatis.config"), MybatisConfiguration.class)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
