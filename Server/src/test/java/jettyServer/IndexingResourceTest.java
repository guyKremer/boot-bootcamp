package jettyServer;

import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexingResourceTest {

    private String uri;

    public IndexingResourceTest(){
        uri = "http://localhost/index";
    }

    @Test
    public void run(){
        Client client = ClientBuilder.newClient();
        index(client);
    }

    public Map index(Client client){
        Map<String,String> reqBody = new HashMap<>();
        Map<String,String> indexedFields = new HashMap<>();
        String message = generateRandomString();
        String header = generateRandomString();

        indexedFields.put("message",message);
        indexedFields.put("header",header);

        try{
            reqBody.put("message",message);
            Response response = client.target(uri)
                    .request()
                    .header("user-agent",header)
                    .post(Entity.json(new ObjectMapper().writeValueAsString(reqBody)));
            Assert.assertEquals(200,response.getStatus());
        }
        catch (IOException e){

        }
        return indexedFields;
    }

    private String generateRandomString (){
        return RandomStringUtils.random(6,true,false);
    }

}