package jettyServer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static jettyServer.TestUtils.generateRandomString;
import static org.awaitility.Awaitility.await;


public class SearchResourceTest {


    @Test
    public void run(){
        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        TestUtils.indexDocument(messageValue,headerValue);

        await().atMost(5,TimeUnit.SECONDS).until(() -> isDocumentIndexed(messageValue,headerValue));
    }
    private boolean isDocumentIndexed(String messageValue,String headerValue) {
        ObjectMapper mapper = new ObjectMapper();
        int expectedHits = 1;

        try{
            Response response = TestUtils.searchDocument(messageValue,headerValue);
            JsonNode root = mapper.readTree(response.readEntity(String.class));
            return root.path("hits").path("total").getValueAsInt() == expectedHits;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}