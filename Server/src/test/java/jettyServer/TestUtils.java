package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class TestUtils {

    private static final String INDEX_URI = "http://localhost/index";
    private static final String SEARCH_URI = "http://localhost/search";


    public static Response indexDocument(String accountToken, String messageValue, String headerValue) {
        Client client = ClientBuilder.newClient();
        Map<String, String> reqBody = new HashMap<>();
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("message", messageValue);
        indexedFields.put("header", headerValue);

        try {
            reqBody.put("message", messageValue);
            Response response = client.target(INDEX_URI)
                    .request()
                    .header("X-ACCOUNT-TOKEN", accountToken)
                    .header("user-agent", headerValue)
                    .post(Entity.json(new ObjectMapper().writeValueAsString(reqBody)));

            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDocumentIndexed(String accountToken, String messageValue, String headerValue) {
        ObjectMapper mapper = new ObjectMapper();
        int expectedHits = 1;

        try {
            Response response = TestUtils.searchDocument(accountToken, messageValue, headerValue);
            JsonNode root = mapper.readTree(response.readEntity(String.class));
            return root.path("hits").path("total").getValueAsInt() == expectedHits;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Response searchDocument(String accountToken, String messageValue, String headerValue) {
        Client client = ClientBuilder.newClient();
        return client.target(SEARCH_URI)
                .queryParam("message", messageValue)
                .queryParam("header", headerValue)
                .request()
                .header("X-ACCOUNT-TOKEN", accountToken)
                .get();
    }

    public static AccountData createRandomAccount(AccountsClient accountsClient) {
        ObjectMapper mapper = new ObjectMapper();
        String accountName = generateRandomString();
        Map<String, String> createAccountRequestAsMap = ImmutableMap.of("accountName", accountName);
        try {
            String createAccountRequestAsJson = mapper.writeValueAsString(createAccountRequestAsMap);
            CreateAccountRequest createAccountRequest = mapper.readValue(createAccountRequestAsJson, CreateAccountRequest.class);
            return accountsClient.createAccount(createAccountRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String generateRandomString() {
        return RandomStringUtils.random(6, true, false);
    }
}
