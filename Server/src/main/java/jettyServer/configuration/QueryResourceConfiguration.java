package jettyServer.configuration;


public class QueryResourceConfiguration {

    private String elasticSearchHost;
    private int elasticSearchPort;


    public int getElasticSearchPort() {
        return elasticSearchPort;
    }

    public String getElasticSearchHost() {
        return elasticSearchHost;
    }
}
