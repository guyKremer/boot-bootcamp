package jettyServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import accounts.pojos.AccountData;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.indexDocument;
import static jettyServer.TestUtils.isDocumentIndexed;
import static org.awaitility.Awaitility.await;

public class MultiTenancyTest {

    @Test
    public void run() {
        AccountData account1 = createRandomAccount();
        AccountData account2 = createRandomAccount();
        String account1Message = generateRandomString();
        String account2Message = generateRandomString();
        String account1header = generateRandomString();
        String account2header = generateRandomString();

        indexDocument(account1.getToken(), account1Message, account1header);
        indexDocument(account2.getToken(), account2Message, account2header);
            /*
                Waiting until the documents are indexed
             */
        await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account1.getToken(), account1Message, account1header));
        await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account2.getToken(), account2Message, account2header));

            /*
                Checks the the log of account 1 is not accessible to account 2 (and the other way around).
             */
        Assert.assertEquals(false, isDocumentIndexed(account1.getToken(), account2Message, account2header));
        Assert.assertEquals(false, isDocumentIndexed(account2.getToken(), account1Message, account1header));
    }
}
