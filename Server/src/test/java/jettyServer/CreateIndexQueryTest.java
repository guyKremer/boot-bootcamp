package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.indexDocument;
import static jettyServer.TestUtils.isDocumentIndexed;
import static org.awaitility.Awaitility.await;

public class CreateIndexQueryTest {

    private static final String ACCOUNTS_MANAGER_URI = "http://localhost:8002/accounts";
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void run() {
        AccountsClient accountsClient = new AccountsClient(ACCOUNTS_MANAGER_URI);
        String accountName = generateRandomString();
        String message = generateRandomString();
        String header = generateRandomString();
        Map<String, String> createAccountRequestAsMap = ImmutableMap.of("accountName", accountName);

        try {
            String createAccountRequestAsJson = mapper.writeValueAsString(createAccountRequestAsMap);
            CreateAccountRequest createAccountRequest = mapper.readValue(createAccountRequestAsJson, CreateAccountRequest.class);

            AccountData createdAccount = accountsClient.createAccount(createAccountRequest);
            indexDocument(createdAccount.getToken(),message,header);

            await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(createdAccount.getToken(),message,header));

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
