package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TestUtils {

    private static final String INDEX_URI = "http://localhost/index";
    private static final String SEARCH_URI = "http://localhost/search";
    private static final String CREATE_ACCOUNT_URI = "http://localhost/create-account";


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
            Response response = searchDocument(accountToken, messageValue, headerValue);
            JsonNode root = mapper.readTree(response.readEntity(String.class));
            return root.path("hits").path("total").asInt() == expectedHits;
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

    public static AccountData createRandomAccount() {
        ObjectMapper mapper = new ObjectMapper();
        String accountName = generateRandomString();
        Map<String, String> createAccountRequestAsMap = ImmutableMap.of("accountName", accountName);
        Client client = ClientBuilder.newClient();

        try {
            String createAccountRequestAsJson = mapper.writeValueAsString(createAccountRequestAsMap);
            CreateAccountRequest createAccountRequest = mapper.readValue(createAccountRequestAsJson, CreateAccountRequest.class);
            Response response = client.target(CREATE_ACCOUNT_URI)
                    .request().post(Entity.json(createAccountRequest));

            String accountDataAsJson = response.readEntity(String.class);
            return mapper.readValue(accountDataAsJson, AccountData.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String generateRandomString() {
        return RandomStringUtils.random(6, true, false);
    }
}
