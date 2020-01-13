package jettyServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import di.ServiceInjectorCreator;
import io.logz.guice.jersey.JerseyServer;
import jettyServer.configuration.ServerConfiguration;
import jettyServer.di.ServerModule;

import java.io.File;


class Main {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Injector injector = ServiceInjectorCreator.createInjector(new ServerModule(mapper.readValue(new File("/usr/server.config"), ServerConfiguration.class)));
        JerseyServer instance = injector.getInstance(JerseyServer.class);
        instance.start();
    }
}
