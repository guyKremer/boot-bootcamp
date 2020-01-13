package jettyServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import accounts.AccountsClient;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.indexDocument;
import static jettyServer.TestUtils.isDocumentIndexed;
import static org.awaitility.Awaitility.await;

public class MultiTenancyTest {
    private static final String ACCOUNTS_MANAGER_URI = "http://localhost:8002/accounts";

    @Test
    public void run() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String account1Name = generateRandomString();
            String account2Name = generateRandomString();
            String account1Message = generateRandomString();
            String account2Message = generateRandomString();
            String account1header = generateRandomString();
            String account2header = generateRandomString();

            String firstCreateAccountRequestAsJson = mapper.writeValueAsString(ImmutableMap.of("accountName", account1Name));
            String secondCreateAccountRequestAsJson = mapper.writeValueAsString(ImmutableMap.of("accountName", account2Name));
            CreateAccountRequest firstCreateAccountRequest = mapper.readValue(firstCreateAccountRequestAsJson, CreateAccountRequest.class);
            CreateAccountRequest secondCreateAccountRequest = mapper.readValue(secondCreateAccountRequestAsJson, CreateAccountRequest.class);

            AccountData account1 = createRandomAccount();
            AccountData account2 = createRandomAccount();
            indexDocument(account1.getToken(), account1Message, account1header);
            indexDocument(account2.getToken(), account2Message, account2header);



            /*
                Waiting until the documents are indexed
             */
            await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account1.getToken(), account1Message, account1header));
            await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account2.getToken(), account2Message, account2header));

            /*
                Checks the the log of account number 1 is not accessible to account number 2 (and the other way around).
             */
            Assert.assertEquals(false,isDocumentIndexed(account1.getToken(),account2Message,account2header));
            Assert.assertEquals(false,isDocumentIndexed(account2.getToken(),account1Message,account1header));

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}
