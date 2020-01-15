package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import common.parsers.JsonParser;
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

        reqBody.put("message", messageValue);
        Response response = client.target(INDEX_URI)
                .request()
                .header("X-ACCOUNT-TOKEN", accountToken)
                .header("user-agent", headerValue)
                .post(Entity.json(JsonParser.writeAsJson(reqBody)));

        return response;

    }

    public static boolean isDocumentIndexed(String accountToken, String messageValue, String headerValue) {
        int expectedHits = 1;
        Response response = searchDocument(accountToken, messageValue, headerValue);
        JsonNode root = JsonParser.readTree(response.readEntity(String.class));

        return root.path("hits").path("total").asInt() == expectedHits;
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
        String accountName = generateRandomString();
        Client client = ClientBuilder.newClient();
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(accountName);
        Response response = client.target(CREATE_ACCOUNT_URI)
                .request().post(Entity.json(createAccountRequest));
        String accountDataAsJson = response.readEntity(String.class);

        return JsonParser.parse(accountDataAsJson, AccountData.class);

    }

    public static String generateRandomString() {
        return RandomStringUtils.random(6, true, false);
    }

    public static String generateRandomToken() {
        return RandomStringUtils.random(33, true, false);
    }
}
