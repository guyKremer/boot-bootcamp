package jettyServer;

import accounts.pojos.AccountData;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status;

import java.util.concurrent.TimeUnit;

import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.generateRandomToken;
import static jettyServer.TestUtils.isDocumentIndexed;
import static org.awaitility.Awaitility.await;


public class SearchResourceTest {

    @Test
    public void sanity() {
        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        AccountData account = createRandomAccount();
        TestUtils.indexDocument(account.getToken(), messageValue, headerValue);

        await().atMost(10, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account.getToken(), messageValue, headerValue));
    }

    @Test
    public void searchWithInvalidToken() {

        Response response = TestUtils.searchDocument(generateRandomToken(), generateRandomString(), generateRandomString());

        Assert.assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

}