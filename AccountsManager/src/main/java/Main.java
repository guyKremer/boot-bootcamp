import com.google.inject.Injector;
import di.AccountsManagerModule;
import di.ServiceInjectorCreator;
import io.logz.guice.jersey.JerseyServer;

public class Main {
    public static void main(String[] args) throws Exception {
        Injector injector = ServiceInjectorCreator.createInjector(new AccountsManagerModule());
        JerseyServer instance = injector.getInstance(JerseyServer.class);
        instance.start();
    }
}
