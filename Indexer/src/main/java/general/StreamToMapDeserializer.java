package general;

import org.apache.kafka.common.serialization.Deserializer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

public class StreamToMapDeserializer implements Deserializer {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Map deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data,0,data.length,Map.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
