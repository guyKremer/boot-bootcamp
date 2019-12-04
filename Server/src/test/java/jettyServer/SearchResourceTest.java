package jettyServer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;


public class SearchResourceTest {

    private String uri;
    private Client client;

    public SearchResourceTest(){
        uri = "http://localhost/search";
        client = ClientBuilder.newClient();
    }

    @Test
    public void run() throws IOException {
        Map indexedFields;
        IndexingResourceTest indexTest = new IndexingResourceTest();

        indexedFields=indexTest.index(client);
        await().atMost(2,TimeUnit.SECONDS).until(documentIndexed(indexedFields));
    }
    private Callable<Boolean> documentIndexed(Map indexedFields)throws IOException {

        return () ->{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            int actualTotalHits = 0 , expectedHits = 1;

            Response response = client.target(uri)
                    .queryParam("message",indexedFields.get("message"))
                    .queryParam("header",indexedFields.get("header"))
                    .request()
                    .get();
            root = mapper.readTree(response.readEntity(String.class));
            actualTotalHits = root.path("hits").path("total").getValueAsInt();
            return actualTotalHits==expectedHits;
        };
    }

}