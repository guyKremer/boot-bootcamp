package jettyServer.configuration;

public class IndexingResourceConfiguration {

    private String brokerHost;
    private int brokerPort;
    private String topic;

    public String getBrokerHost() {

        return brokerHost;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public String getTopic() {
        return topic;
    }
}
