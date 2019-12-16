package jettyServer.configuration;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;

public class ServerConfiguration {
    private String logMessage;
    private String elasticSearchHost;
    private int elasticSearchPort;

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getElasticSearchHost() { return elasticSearchHost; }


    public void setElasticSearchHost(String elasticSearchHost) {
        this.elasticSearchHost = elasticSearchHost;
    }

    public int getGetElasticSearchPort() { return elasticSearchPort; }


    public void setElasticSearchPort(int elasticSearchPort) {
        this.elasticSearchPort = elasticSearchPort;
    }
}
