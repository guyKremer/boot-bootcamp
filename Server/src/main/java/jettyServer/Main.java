package jettyServer;

import com.google.inject.Injector;
import common.parsers.JsonParser;
import di.ServiceInjectorCreator;
import io.logz.guice.jersey.JerseyServer;
import jettyServer.configuration.ServerConfiguration;
import jettyServer.di.ServerModule;

import java.io.File;


class Main {
    public static void main(String[] args) throws Exception {
        Injector injector = ServiceInjectorCreator.createInjector(new ServerModule(JsonParser.parse(new File("/usr/server.config"), ServerConfiguration.class)));
        JerseyServer instance = injector.getInstance(JerseyServer.class);
        instance.start();
    }
}
