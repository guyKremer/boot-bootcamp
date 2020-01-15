package di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import common.parsers.JsonParser;
import configuration.MybatisConfiguration;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

import java.io.File;

public class AccountsManagerModule extends AbstractModule {

    public AccountsManagerModule() {

    }

    @Override
    protected void configure() {
        ObjectMapper mapper = new ObjectMapper();
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("jettyServer.resources")
                .addPort(80)
                .withContextPath("/*")
                .build();

        install(new JerseyModule(configuration));
        install(new MyBatisAccountsModule(JsonParser.parse(new File("/usr/myBatis.config"), MybatisConfiguration.class)));
    }
}
