import Indexer.Indexer;
import com.google.inject.Guice;
import com.google.inject.Injector;


class Main {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new IndexerModule());
        Indexer indexer = injector.getInstance(Indexer.class);
        indexer.run();
    }
}