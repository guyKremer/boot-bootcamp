package jettyServer;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;


import static jettyServer.TestUtils.generateRandomString;

public class IndexResourceTest {

    private String elasticHostUri;

    public IndexResourceTest(){
        elasticHostUri = "http://localhost/index";
    }

    @Test
    public void run(){

        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        Response response = TestUtils.indexDocument(messageValue,headerValue);

        Assert.assertEquals(200,response.getStatus());
    }

}
