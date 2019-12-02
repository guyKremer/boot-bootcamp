package jettyServer;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import java.util.Map;

@Path("index")
public class IndexingResource {
    private RestHighLevelClient elasticClient;

    public IndexingResource(){
         elasticClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("elasticSearch", 9200, "http")));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String indexPost(Map<String,String> body, @HeaderParam("user-agent") String userAgent){
        try{
            body.put("header",userAgent);
            IndexRequest indexRequest = new IndexRequest("bootcamp","_doc").source(body);
            IndexResponse response= elasticClient.index(indexRequest);
            return response.status().toString();
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
}
