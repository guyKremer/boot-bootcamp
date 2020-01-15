package kafka;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KafkaRecord implements Serializable {

    private String accountToken;
    private Map<String,String> source = new HashMap<>();

    public KafkaRecord(){

    }

    public KafkaRecord(String accountToken ,String message,String header) {
        this.accountToken = accountToken;
        this.source.put("message",message);
        this.source.put("header",header);
    }

    public String getAccountToken() {
        return accountToken;
    }

    public Map<String,String> getSource() {
        return source;
    }

}
