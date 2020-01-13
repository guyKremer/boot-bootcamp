package jettyServer.configuration;

public class IndexingResourceConfiguration {

    private String brokerHost;
    private int brokerPort;
    private String kafkaTopic;

    public String getBrokerHost() {

        return brokerHost;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }
}
