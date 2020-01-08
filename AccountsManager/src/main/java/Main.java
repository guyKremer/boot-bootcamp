import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.MybatisConfiguration;
import di.AccountsManagerModule;
import di.MyBatisAccountsModule;
import io.logz.guice.jersey.JerseyServer;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Injector injector = Guice.createInjector(new AccountsManagerModule(), new MyBatisAccountsModule(mapper.readValue(new File("/usr/myBatis.config"), MybatisConfiguration.class)));
        JerseyServer instance = injector.getInstance(JerseyServer.class);
        instance.start();
    }
}
