package jettyServer.resources;

import accounts.pojos.AccountData;
import accounts.pojos.CreateAccountRequest;
import common.api.RequestConstants;
import db.mappers.AccountsMapper;
import org.apache.commons.lang3.RandomStringUtils;

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

@Path("accounts")
public class AccountsManagerResource {

    private final AccountsMapper accountsMapper;


    @Inject
    public AccountsManagerResource(AccountsMapper accountsMapper) {
        this.accountsMapper = accountsMapper;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountRequest body) {
        AccountData accountData = new AccountData(body.getAccountName(),RandomStringUtils.random(33, true, false).toLowerCase(),RandomStringUtils.random(33, true, false));
        accountsMapper.insertAccount(accountData);
        return Response.status(Response.Status.CREATED).entity(accountData).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@Context HttpHeaders httpHeaders) {
        String token = httpHeaders.getHeaderString(RequestConstants.ACCOUNT_TOKEN_KEY);
        AccountData accountData = accountsMapper.selectAccount(token);
        if (accountData == null) {
            throw new NotAuthorizedException("Token unauthorized");
        }
        return Response.status(Response.Status.OK).entity(accountData).build();
    }
}
