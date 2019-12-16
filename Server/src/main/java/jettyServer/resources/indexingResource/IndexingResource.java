package jettyServer.resources.indexingResource;


import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import jettyServer.configuration.ServerConfiguration;
import kafka.ObjectSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Path("index")
public class IndexingResource {
    private final ServerConfiguration serverConfiguration;
    private final  Producer producer;


    @Inject
    public IndexingResource(ServerConfiguration serverConfiguration, Producer producer){
        this.serverConfiguration = serverConfiguration;
        this.producer=producer;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(IndexingRequestObj body, @HeaderParam("user-agent") String userAgent) {
        Map source = createSource(body.getMessage(),userAgent);
        String topic = "bootcamp";

        ProducerRecord<String, Map> record = new ProducerRecord<>(topic, source);
        try{
            producer.send(record).get();
            return Response.ok().entity("OK").build();
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getCause()).build();
        }

    }

    private Map<String,Object> createSource(String message, String userAgent) {
        Map<String,Object> source = new HashMap<>();

        source.put("message",message);
        source.put("header",userAgent);
        return source;
    }

    private KafkaProducer createKafkaProducer(String brokerHost,int brokerPort){

        String kafkaUri = brokerHost+":"+brokerPort;

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaUri);
        props.put("acks", "all");
        props.put("key.serializer", ObjectSerializer.class.getName());
        props.put("value.serializer", ObjectSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}
