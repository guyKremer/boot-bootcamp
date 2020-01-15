package configuration;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class MybatisConfiguration {

    private String  mybatisEnvironmentId;

    private String  schema;

    private String  dataBaseUsername;

    private String  dataBaseUserPassword;

    private boolean autoCommit;

    private String  dataBaseHost;

    private int dataBasePort;


    public String getMybatisEnvironmentId() {
        return mybatisEnvironmentId;
    }

    public int getDataBasePort() {
        return dataBasePort;
    }

    public String getDataBaseHost() {
        return dataBaseHost;
    }

    public String getDataBaseUserPassword() {
        return dataBaseUserPassword;
    }

    public String getDataBaseUsername() {
        return dataBaseUsername;
    }

    public String getSchema() {
        return schema;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

}

