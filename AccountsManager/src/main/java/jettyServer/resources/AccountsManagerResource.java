package jettyServer.resources;

import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import db.dao.AccountsDao;
import exceptions.DbAccessException;
import org.apache.ibatis.exceptions.PersistenceException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("accounts")
public class AccountsManagerResource {

    private final AccountsDao accountsDao;


    @Inject
    public AccountsManagerResource(AccountsDao accountsDao) {
        this.accountsDao = accountsDao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountRequest body) {
        AccountData accountData = new AccountData(body.getAccountName());
        try{
            accountsDao.insertAccount(accountData);
        }
        catch (DbAccessException dbe){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }
        return Response.status(Response.Status.CREATED).entity(accountData).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@Context HttpHeaders httpHeaders) {
        String token = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");
        try{
            AccountData accountData = accountsDao.selectAccount(token);
            if (accountData == null) {
                throw new NotAuthorizedException("Token unauthorized");
            }
            return Response.status(Response.Status.OK).entity(accountData).build();
        }
        catch (DbAccessException dbe){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
