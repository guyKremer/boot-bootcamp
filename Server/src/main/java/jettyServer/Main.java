package jettyServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.JerseyServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

class Main {
    public static void main(String[] args) throws Exception{
        Injector injector = Guice.createInjector(new ServerModule());
        JerseyServer instance = injector.getInstance(JerseyServer.class);
        instance.start();
    }
}
