package accounts;


import accounts.exceptions.DbAccessException;
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

    private final String accountsManagerUri;
    ObjectMapper mapper = new ObjectMapper();


    public AccountsClient(String accountsManagerUri) {
        this.accountsManagerUri = accountsManagerUri;
    }

    public AccountData getAccount(String accountToken){
        Client client = ClientBuilder.newClient();
        Response response = client.target(accountsManagerUri)
                    .request().header("X-ACCOUNT-TOKEN",accountToken).get();

        if( response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)){
                throw new NotAuthorizedException("Token not authorized");
        }

        else {
            try{
                String accountDataAsJson = response.readEntity(String.class);
                return mapper.readValue(accountDataAsJson,AccountData.class);
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isAuthenticated(String accountToken){
        Client client = ClientBuilder.newClient();
        Response response = client.target(accountsManagerUri)
                .request().header("X-ACCOUNT-TOKEN",accountToken).get();

        if(response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)){
            return false;
        }

        else {
            return true;
        }
    }

    public AccountData createAccount(CreateAccountRequest createAccountRequest){

        Client client = ClientBuilder.newClient();
        Response response = client.target(accountsManagerUri)
                .request().post(Entity.json(createAccountRequest));

        try {
            String accountDataAsJson = response.readEntity(String.class);
            return mapper.readValue(accountDataAsJson, AccountData.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
