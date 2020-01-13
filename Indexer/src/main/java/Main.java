import Indexer.Indexer;
import di.IndexerModule;
import com.google.inject.Injector;
import di.ServiceInjectorCreator;


class Main {

    public static void main(String[] args){
        Injector injector = ServiceInjectorCreator.createInjector(new IndexerModule());
        Indexer indexer = injector.getInstance(Indexer.class);
        indexer.run();
    }
}