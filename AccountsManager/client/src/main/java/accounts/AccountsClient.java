package accounts;


import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.DbAccessException;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.xml.bind.DataBindingException;
import java.io.IOException;
import java.util.Optional;

public class AccountsClient {

    private final String accountsManagerBaseUri;
    ObjectMapper mapper = new ObjectMapper();


    public AccountsClient(String accountsManagerBaseUri) {
        this.accountsManagerBaseUri = accountsManagerBaseUri;
    }

    public Optional<AccountData> getAccount(String accountToken) throws DbAccessException {
        Client client = ClientBuilder.newClient();
        String getAccountUri = accountsManagerBaseUri + "/accounts";
        Response response = client.target(getAccountUri)
                .request().header("X-ACCOUNT-TOKEN", accountToken).get();

        if (response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)) {
            return Optional.ofNullable(null);
        }
        else if (!response.getStatusInfo().equals(Response.Status.OK)) {
            throw new DbAccessException("Couldn't retrieve account");
        }
        else {
            try {
                String accountDataAsJson = response.readEntity(String.class);
                return Optional.ofNullable(mapper.readValue(accountDataAsJson, AccountData.class));
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    public boolean isAuthenticated(String accountToken) throws DbAccessException {
        Client client = ClientBuilder.newClient();
        String getAccountUri = accountsManagerBaseUri + "/accounts";
        Response response = client.target(getAccountUri)
                .request().header("X-ACCOUNT-TOKEN", accountToken).get();

        if (!response.getStatusInfo().equals(Response.Status.OK)) {
            throw new DbAccessException("Couldn't retrieve account");
        }
        else if (response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)) {
            return false;
        }
        else {
            return true;
        }
    }

    public AccountData createAccount(CreateAccountRequest createAccountRequest) throws DbAccessException {
        Client client = ClientBuilder.newClient();
        String createAccountUri = accountsManagerBaseUri + "/accounts";

        Response response = client.target(createAccountUri)
                .request().post(Entity.json(createAccountRequest));

        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            throw new DbAccessException("Couldn't create account " + createAccountRequest.getAccountName());
        }

        try {
            String accountDataAsJson = response.readEntity(String.class);
            return mapper.readValue(accountDataAsJson, AccountData.class);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
