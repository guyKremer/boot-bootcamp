package jettyServer;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;


import static jettyServer.TestUtils.createRandomAccount;
import static jettyServer.TestUtils.generateRandomString;

public class IndexResourceTest {

    private static final String ACCOUNTS_MANAGER_URI = "http://localhost:8002/accounts";


    @Test
    public void run(){
        AccountsClient accountsClient = new AccountsClient(ACCOUNTS_MANAGER_URI);
        String messageValue = generateRandomString();
        String headerValue = generateRandomString();
        AccountData account = createRandomAccount(accountsClient);
        Response response = TestUtils.indexDocument(account.getToken(),messageValue,headerValue);

        Assert.assertEquals(200,response.getStatus());
    }

}
