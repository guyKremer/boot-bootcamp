package common.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonParser {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T parse(String json, Class<T> type) {

        try {
            return mapper.readValue(json, type);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static <T> T parse(File jsonFile, Class<T> type) {

        try {
            return mapper.readValue(jsonFile, type);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static String writeAsJson(Object object) {

        try {
            return mapper.writeValueAsString(object);

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static JsonNode readTree(String json){
        try{
            return mapper.readTree(json);
        }
        catch (IOException ioe){
            throw new RuntimeException(ioe);
        }
    }


}
