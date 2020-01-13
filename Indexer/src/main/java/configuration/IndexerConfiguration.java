package configuration;

public class IndexerConfiguration {

    private String brokerHost;
    private int brokerPort;
    private String topic;
    private long intervalForCommit;
    private long intervalForPoll;
    private String elasticSearchHost;
    private int elasticSearchPort;
    private String groupId;
    private String accountsClientHost;


    public String getBrokerHost() {
        return brokerHost;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTopic() {
        return topic;
    }


    public long getIntervalForCommit() {
        return intervalForCommit;
    }

    public long getIntervalForPoll() {
        return intervalForPoll;
    }


    public String getElasticSearchHost() {
        return elasticSearchHost;
    }


    public int getElasticSearchPort() {
        return elasticSearchPort;
    }

    public String getAccountsClientHost() {
        return accountsClientHost;
    }
}
