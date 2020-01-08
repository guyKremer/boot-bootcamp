package accounts;


import accounts.exceptions.OperationFailedException;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import org.codehaus.jackson.map.ObjectMapper;

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

    public AccountData getAccount(String accountToken) throws OperationFailedException{
        Client client = ClientBuilder.newClient();
        Response response = client.target(accountsManagerUri)
                    .request().header("X-ACCOUNT-TOKEN",accountToken).get();

        if( response.getStatusInfo().equals(Response.Status.UNAUTHORIZED)){
            throw new NotAuthorizedException(response.readEntity(String.class));
        }

        else {
            try{
                String accountDataAsJson = response.readEntity(String.class);
                return mapper.readValue(accountDataAsJson,AccountData.class);
            }
            catch (Exception e){
                throw  new RuntimeException(e);
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

        if( !response.getStatusInfo().equals(Response.Status.CREATED)){
            throw new RuntimeException();
        }

        try {
            String accountDataAsJson = response.readEntity(String.class);
            return mapper.readValue(accountDataAsJson, AccountData.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
