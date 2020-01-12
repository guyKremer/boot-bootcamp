package jettyServer.resources.accounts;

import jettyServer.resources.accounts.pojos.CreateAccountRequestObj;
import org.apache.commons.lang3.RandomStringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public class AccountsResource {

    @POST
    @Path("create-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createAccount(CreateAccountRequestObj body) {

    }
}
