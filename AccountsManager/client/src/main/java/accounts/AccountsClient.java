package accounts;


import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.api.RequestConstants;
import common.parsers.JsonParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class AccountsClient {

    private final String accountsManagerBaseUri;
    private final Client client;


    public AccountsClient(String accountsManagerBaseUri) {
        this.accountsManagerBaseUri = accountsManagerBaseUri;
        this.client = ClientBuilder.newClient();

    }

    public Optional<AccountData> getAccount(String accountToken) {
        String getAccountUri = accountsManagerBaseUri + "/accounts";
        Response response = client.target(getAccountUri)
                .request().header(RequestConstants.ACCOUNT_TOKEN_KEY, accountToken).get();

        if (response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)) {
            return Optional.empty();
        } else if (!response.getStatusInfo().equals(Response.Status.OK)) {
            throw new RuntimeException();
        }
        String accountDataAsJson = response.readEntity(String.class);
        return Optional.of(JsonParser.parse(accountDataAsJson, AccountData.class));

    }

    public AccountData createAccount(CreateAccountRequest createAccountRequest) {
        String createAccountUri = accountsManagerBaseUri + "/accounts";

        Response response = client.target(createAccountUri)
                .request().post(Entity.json(createAccountRequest));

        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            throw new RuntimeException();
        }

        String accountDataAsJson = response.readEntity(String.class);
        return JsonParser.parse(accountDataAsJson, AccountData.class);
    }
}
