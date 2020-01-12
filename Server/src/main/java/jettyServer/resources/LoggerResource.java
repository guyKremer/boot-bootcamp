package jettyServer.resources;

import jettyServer.configuration.ServerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("boot-bootcamp")
public class LoggerResource {
    private final ServerConfiguration serverConfiguration;
    private static int i = 0;
    private static Double containerKey = Math.random();

    @Inject
    public LoggerResource(ServerConfiguration serverconfiguration) {
        this.serverConfiguration = serverconfiguration;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendLogs() {
        Logger logger = LogManager.getLogger(LoggerResource.class);
        String logMsg = "boot-boot" + " " + (++i) + "   " + containerKey.toString();
        logger.info(logMsg);
        return Response.ok().build();
    }
}
