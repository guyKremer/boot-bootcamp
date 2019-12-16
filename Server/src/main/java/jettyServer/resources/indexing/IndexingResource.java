package jettyServer.resources.indexing;


import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.HashMap;
import java.util.Map;

@Path("index")
public class IndexingResource {
    private final RestHighLevelClient elasticClient;
    private final IndexRequest indexRequest;


    @Inject
    public IndexingResource(RestHighLevelClient restHighLevelClient,IndexRequest indexRequest){
        this.elasticClient = restHighLevelClient;
        this.indexRequest = indexRequest;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(RequestObj body, @HeaderParam("user-agent") String userAgent){
        Map<String,String> source = new HashMap<>();
        try{
            source.put("message",body.getMessage());
            source.put("header",userAgent);
            IndexResponse response= elasticClient.index(indexRequest.source(source));
            return Response.ok().entity(response.status().toString()).build();
        }
        catch (Exception e){
           return Response.status(Response.Status.BAD_REQUEST).entity(e.getStackTrace()).build();
        }
    }
}
