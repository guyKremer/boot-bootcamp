package jettyServer.resources;

import accounts.pojos.CreateAccountRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import db.dao.AccountsDao;
import di.MyBatisAccountsModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.ibatis.exceptions.PersistenceException;
import pojos.AccountData;

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

@Path("accounts")
public class AccountsManagerResource {

    private final AccountsDao accountsDao;


    @Inject
    public AccountsManagerResource(AccountsDao accountsDao){
        this.accountsDao = accountsDao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountRequest body) {
        try {
            AccountData accountData = new AccountData(body.getAccountName());
            accountsDao.insertAccount(accountData);
            return Response.status(Response.Status.CREATED).entity(accountData).build();
        }
        catch (Exception e){
           throw  new InternalServerErrorException();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@Context HttpHeaders httpHeaders){

        String token = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");
        AccountData accountData = accountsDao.selectAccount(token);

        if(accountData == null){
            throw new NotAuthorizedException("Token unauthorized");
        }

        return Response.status(Response.Status.OK).entity(accountData).build();
    }
}
