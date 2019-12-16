package jettyServer;

import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    public static Response indexDocument(String elasticHostUri,String messageValue,String headerValue){
        Client client = ClientBuilder.newClient();
        Map<String,String> reqBody = new HashMap<>();
        Map<String,String> indexedFields = new HashMap<>();


        indexedFields.put("message",messageValue);
        indexedFields.put("header",headerValue);

        try{
            reqBody.put("message",messageValue);
            Response response = client.target(elasticHostUri)
                    .request()
                    .header("user-agent",headerValue)
                    .post(Entity.json(new ObjectMapper().writeValueAsString(reqBody)));
            return response;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static Response searchDocument(String elasticHostUri,String messageValue,String headerValue){
        Client client = ClientBuilder.newClient();
         return client.target(elasticHostUri)
                    .queryParam("message", messageValue)
                    .queryParam("header", headerValue)
                    .request()
                    .get();
    }

    public static String generateRandomString (){
        return RandomStringUtils.random(6,true,false);
    }
}
