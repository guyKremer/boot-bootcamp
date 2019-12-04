package jettyServer.resources;


import com.google.inject.Guice;
import jettyServer.configuration.ServerConfiguration;
import logSender.LogSender;
import logSender.LogSenderModule;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.inject.Injector;

@Path("boot-bootcamp")
public class loggerResource {
    private ServerConfiguration serverConfiguration;
    private static int i=0;
    private static Double containerKey=Math.random();
    private Injector logSenderInjector = Guice.createInjector(new LogSenderModule());

    @Inject
    public loggerResource(ServerConfiguration serverconfiguration) {
        this.serverConfiguration = serverconfiguration;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sendLogs(){
        LogSender logSender = logSenderInjector.getInstance(LogSender.class);
        String logMsg = "boot boot "+(++i) +"   "+containerKey.toString();
        logSender.sendLog(logMsg);
        return "OK";
    }
}
