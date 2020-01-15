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

import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.indexDocument;
import static jettyServer.TestUtils.isDocumentIndexed;
import static org.awaitility.Awaitility.await;

public class CreateIndexQueryTest {



    @Test
    public void run() {
        String message = generateRandomString();
        String header = generateRandomString();
        AccountData createdAccount = createRandomAccount();

        indexDocument(createdAccount.getToken(), message, header);

        await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(createdAccount.getToken(), message, header));
    }
}
