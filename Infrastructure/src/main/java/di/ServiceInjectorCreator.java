package di;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


public class ServiceInjectorCreator {

    public static Injector createInjector(Module module) {
        return Guice.createInjector(module, new RequireExplicitBindingsModule());
    }
}
