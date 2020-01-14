package jettyServer.resources.indexingResource;


import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.InternalServerErrorException;
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
import exceptions.DbAccessException;
import jettyServer.configuration.IndexingResourceConfiguration;
import kafka.KafkaRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.errors.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static java.util.Objects.requireNonNull;


@Path("index")
public class IndexingResource {
    private final IndexingResourceConfiguration indexingResourceConfiguration;
    private final Producer producer;
    private final AccountsClient accountsClient;



    @Inject
    public IndexingResource(IndexingResourceConfiguration indexingResourceConfiguration,AccountsClient accountsClient ,Producer producer) {
        this.indexingResourceConfiguration = requireNonNull(indexingResourceConfiguration);
        this.producer = requireNonNull(producer);
        this.accountsClient = requireNonNull(accountsClient);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(IndexingRequestObj body, @HeaderParam("user-agent") String userAgent,@Context HttpHeaders httpHeaders) {
        ObjectMapper objectMapper = new ObjectMapper();
        String accountToken = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");

        try{
            boolean authenticated = accountsClient.isAuthenticated(accountToken);
            if( ! authenticated ){
                throw new NotAuthorizedException("Token not authorized");
            }
            KafkaRecord kafkaRecord = new KafkaRecord(accountToken,body.getMessage(),userAgent);
            String kafkaRecordAsJson = objectMapper.writeValueAsString(kafkaRecord);
            ProducerRecord<String, String> record = new ProducerRecord<>(indexingResourceConfiguration.getKafkaTopic(), kafkaRecordAsJson);

            producer.send(record);
            return Response.ok().build();
        }
        catch (IOException | DbAccessException e){
            throw new InternalServerErrorException();
        }
    }
}
