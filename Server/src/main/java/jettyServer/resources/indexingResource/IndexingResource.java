package jettyServer.resources.indexingResource;


import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import jettyServer.configuration.IndexingResourceConfiguration;
import jettyServer.configuration.ServerConfiguration;
import kafka.ObjectSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNull;


@Path("index")
public class IndexingResource {
    private final IndexingResourceConfiguration indexingResourceConfiguration;
    private final Producer producer;


    @Inject
    public IndexingResource(IndexingResourceConfiguration indexingResourceConfiguration, Producer producer) {
        this.indexingResourceConfiguration = requireNonNull(indexingResourceConfiguration);
        this.producer = requireNonNull(producer);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(IndexingRequestObj body, @HeaderParam("user-agent") String userAgent) {
        Map<String, Object> source = createSource(body.getMessage(), userAgent);

        ProducerRecord<String, Map> record = new ProducerRecord<>(indexingResourceConfiguration.getTopic(), source);
        producer.send(record);
        return Response.ok().entity("OK").build();
    }

    private Map<String, Object> createSource(String message, String userAgent) {
        Map<String, Object> source = new HashMap<>();

        source.put("message", message);
        source.put("header", userAgent);
        return source;
    }
}
