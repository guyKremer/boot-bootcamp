package jettyServer.resources.indexingResource;


import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import accounts.AccountsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jettyServer.configuration.ServerConfiguration;
import kafka.KafkaRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

@Path("index")
public class IndexingResource {
    private final ServerConfiguration serverConfiguration;
    private final  Producer producer;
    private final AccountsClient accountsClient;


    @Inject
    public IndexingResource(ServerConfiguration serverConfiguration, AccountsClient accountsClient, Producer producer){
        this.serverConfiguration = serverConfiguration;
        this.producer=producer;
        this.accountsClient = accountsClient;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(IndexingRequestObj body, @HeaderParam("user-agent") String userAgent,@Context HttpHeaders httpHeaders) {
        ObjectMapper objectMapper = new ObjectMapper();
        String accountToken = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");
        boolean authenticated = accountsClient.isAuthenticated(accountToken);

        if(! authenticated ){
            throw new NotAuthorizedException("Token not authorized");
        }

        try{
            KafkaRecord kafkaRecord = new KafkaRecord(accountToken,body.getMessage(),userAgent);
            String kafkaRecordAsJson = objectMapper.writeValueAsString(kafkaRecord);
            ProducerRecord<String, String> record = new ProducerRecord<>(serverConfiguration.getKafkaTopic(), kafkaRecordAsJson);

            producer.send(record).get();
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getCause()).build();
        }
    }
}
