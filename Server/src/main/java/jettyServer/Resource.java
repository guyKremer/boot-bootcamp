package jettyServer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("boot-bootcamp")
public class Resource {

    private static int i =0;
    private static Double containerKey = Math.random();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sendLogs(){
        String logMsg = "boot boot "+(++i) +"   "+containerKey.toString();
        Logger logger = LogManager.getLogger(Resource.class);
        logger.warn(logMsg);
        return logMsg;
    }
}
