package kafka;

import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

public class ObjectSerializer implements Serializer {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] retVal = objectMapper.writeValueAsBytes(data);
            return retVal;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
