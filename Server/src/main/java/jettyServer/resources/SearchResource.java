package jettyServer.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;

@Path("search")
public class SearchResource {
    private RestHighLevelClient elasticClient;

    @Inject
    public SearchResource(RestHighLevelClient restHighLevelClient) {
        this.elasticClient = restHighLevelClient;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String query(@QueryParam("message") String message, @QueryParam("header") String header) {
        try {
            SearchRequest searchRequest = new SearchRequest();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder qb =  new BoolQueryBuilder();
            qb.must(QueryBuilders.matchQuery("message", message));
            qb.must(QueryBuilders.matchQuery("header",header));
            searchSourceBuilder.query(qb);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = elasticClient.search(searchRequest);
            JSONObject SRJSON = new JSONObject(searchResponse.toString());
            return SRJSON.toString();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

}
