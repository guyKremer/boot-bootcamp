package jettyServer;

import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.slf4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    public static final String INDEX_URI = "http://localhost/index";
    public static final String SEARCH_URI = "http://localhost/search";

    public static Response indexDocument(String messageValue,String headerValue){
        Client client = ClientBuilder.newClient();
        Map<String,String> reqBody = new HashMap<>();
        Map<String,String> indexedFields = new HashMap<>();

        indexedFields.put("message",messageValue);
        indexedFields.put("header",headerValue);

        try{
            reqBody.put("message",messageValue);
            Response response = client.target(INDEX_URI)
                    .request()
                    .header("user-agent",headerValue)
                    .post(Entity.json(new ObjectMapper().writeValueAsString(reqBody)));
            return response;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static Response searchDocument(String messageValue,String headerValue){
        Client client = ClientBuilder.newClient();
         return client.target(SEARCH_URI)
                    .queryParam("message", messageValue)
                    .queryParam("header", headerValue)
                    .request()
                    .get();
    }

    public static String generateRandomString (){
        return RandomStringUtils.random(6,true,false);
    }
}
