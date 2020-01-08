package accounts.exceptions;

import javax.ws.rs.core.Response;

public class OperationFailedException extends RuntimeException {

    private final Response response;

    public OperationFailedException(int status,String errorMessage){
        this.response = Response.status(status).entity(errorMessage).build();
    }

    public Response getResponse() {
        return response;
    }
}
