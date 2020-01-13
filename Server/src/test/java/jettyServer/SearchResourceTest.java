package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.isDocumentIndexed;
import static org.awaitility.Awaitility.await;


public class SearchResourceTest {

    private static final String ACCOUNTS_MANAGER_URI = "http://localhost:8002/accounts";


    @Test
    public void run(){
        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        AccountsClient accountsClient = new AccountsClient(ACCOUNTS_MANAGER_URI);
        AccountData account = createRandomAccount();
        TestUtils.indexDocument(account.getToken(),messageValue,headerValue);

        await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account.getToken(),messageValue,headerValue));
    }

}