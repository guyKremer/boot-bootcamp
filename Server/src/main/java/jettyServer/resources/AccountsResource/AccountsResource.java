package jettyServer.resources.AccountsResource;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/")
public class AccountsResource {


    private final AccountsClient accountsClient;

    @Inject
    public AccountsResource(AccountsClient accountsClient){
        this.accountsClient = accountsClient;
    }


    @POST
    @Path("create-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(CreateAccountRequest body){

        try{
            AccountData account = accountsClient.createAccount(body);
            return Response.status(Response.Status.CREATED).entity(account).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Couldn't create account").build();
        }
    }

    @GET
    @Path("get-account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@Context HttpHeaders httpHeaders) {
        String accountToken = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");

        try {
            AccountData account = accountsClient.getAccount(accountToken);
            return Response.status(Response.Status.OK).entity(account).build();
        }
        catch (NotAuthorizedException noe){
            throw noe;
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Couldn't retrieve account").build();
        }
    }
}
