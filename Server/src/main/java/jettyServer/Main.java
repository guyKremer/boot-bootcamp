package jettyServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyServer;


class Main {
    public static void main(String[] args) throws Exception{
        Injector injector = Guice.createInjector(new ServerModule());
        JerseyServer instance = injector.getInstance(JerseyServer.class);
        instance.start();
    }
}
