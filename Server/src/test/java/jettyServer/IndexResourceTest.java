package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status;


import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;
import static jettyServer.TestUtils.generateRandomToken;

public class IndexResourceTest {


    @Test
    public void sanity(){
        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        AccountData account = createRandomAccount();
        Response response = TestUtils.indexDocument(account.getToken(),messageValue,headerValue);

        Assert.assertEquals(200,response.getStatus());
    }

    @Test
    public void indexWithWrongToken(){
        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        Response response = TestUtils.indexDocument(generateRandomToken(),messageValue,headerValue);

        Assert.assertEquals(Status.UNAUTHORIZED.getStatusCode(),response.getStatus());
    }

}
