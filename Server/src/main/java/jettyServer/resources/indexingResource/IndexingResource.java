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
import accounts.pojos.AccountData;
import common.api.RequestConstants;
import common.parsers.JsonParser;
import jettyServer.configuration.IndexingResourceConfiguration;
import kafka.KafkaRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


import java.util.Optional;

import static java.util.Objects.requireNonNull;


@Path("index")
public class IndexingResource {
    private final IndexingResourceConfiguration indexingResourceConfiguration;
    private final Producer producer;
    private final AccountsClient accountsClient;


    @Inject
    public IndexingResource(IndexingResourceConfiguration indexingResourceConfiguration, AccountsClient accountsClient, Producer producer) {
        this.indexingResourceConfiguration = requireNonNull(indexingResourceConfiguration);
        this.producer = requireNonNull(producer);
        this.accountsClient = requireNonNull(accountsClient);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(IndexingRequestObj body, @HeaderParam("user-agent") String userAgent, @Context HttpHeaders httpHeaders) {

        String accountToken = httpHeaders.getHeaderString(RequestConstants.ACCOUNT_TOKEN_KEY);

        Optional<AccountData> optionalAccountData = accountsClient.getAccount(accountToken);
        if (!optionalAccountData.isPresent()) {
            throw new NotAuthorizedException("Token not authorized");
        }
        else{
            KafkaRecord kafkaRecord = new KafkaRecord(accountToken, body.getMessage(), userAgent);
            String kafkaRecordAsJson = JsonParser.writeAsJson(kafkaRecord);
            ProducerRecord<String, String> record = new ProducerRecord<>(indexingResourceConfiguration.getKafkaTopic(), kafkaRecordAsJson);
            producer.send(record);

            return Response.ok().build();
        }
    }
}
