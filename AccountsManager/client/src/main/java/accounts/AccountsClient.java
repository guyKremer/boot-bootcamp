package accounts;


import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class AccountsClient {

    private final String accountsManagerBaseUri;
    ObjectMapper mapper = new ObjectMapper();


    public AccountsClient(String accountsManagerBaseUri) {
        this.accountsManagerBaseUri = accountsManagerBaseUri;
    }

    public AccountData getAccount(String accountToken) {
        Client client = ClientBuilder.newClient();
        String getAccountUri = accountsManagerBaseUri + "/accounts";
        Response response = client.target(getAccountUri)
                .request().header("X-ACCOUNT-TOKEN", accountToken).get();

        if (response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)) {
            throw new NotAuthorizedException("Token not authorized");
        } else {
            try {
                String accountDataAsJson = response.readEntity(String.class);
                return mapper.readValue(accountDataAsJson, AccountData.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isAuthenticated(String accountToken) {
        Client client = ClientBuilder.newClient();
        String getAccountUri = accountsManagerBaseUri + "/accounts";
        Response response = client.target(getAccountUri)
                .request().header("X-ACCOUNT-TOKEN", accountToken).get();

        if (response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)) {
            return false;
        } else {
            return true;
        }

    }

    public AccountData createAccount(CreateAccountRequest createAccountRequest) {
        Client client = ClientBuilder.newClient();
        String createAccountUri = accountsManagerBaseUri + "/accounts";

        Response response = client.target(createAccountUri)
                .request().post(Entity.json(createAccountRequest));

        try {
            String accountDataAsJson = response.readEntity(String.class);
            return mapper.readValue(accountDataAsJson, AccountData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
