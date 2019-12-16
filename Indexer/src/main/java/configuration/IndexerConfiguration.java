package configuration;

import java.util.ArrayList;
import java.util.List;

public class IndexerConfiguration {

      private String brokerHost;
      private int brokerPort;
      private String topic;
      private long intervalForCommit;
      private long intervalForPoll;
      private String elasticSearchHost;
      private int elasticSearchPort;


    public String getBrokerHost() {
        return brokerHost;
    }

    public void setBrokerHost(String brokerHost) {
        this.brokerHost = brokerHost;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(int brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getIntervalForCommit() {
        return intervalForCommit;
    }

    public void setIntervalForCommit(long intervalForCommit) {
        this.intervalForCommit = intervalForCommit;
    }

    public long getIntervalForPoll() {
        return intervalForPoll;
    }

    public void setIntervalForPoll(long intervalForPoll) {
        this.intervalForPoll = intervalForPoll;
    }

    public String getElasticSearchHost() {
        return elasticSearchHost;
    }

    public void setElasticSearchHost(String elasticSearchHost) {
        this.elasticSearchHost = elasticSearchHost;
    }

    public int getElasticSearchPort() {
        return elasticSearchPort;
    }

    public void setElasticSearchPort(int elasticSearchPort) {
        this.elasticSearchPort = elasticSearchPort;
    }
}
