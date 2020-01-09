package jettyServer.resources.queryResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;

import static java.util.Objects.requireNonNull;


import java.io.IOException;

@Path("search")
public class QueryResource {
    private final RestHighLevelClient elasticClient;

    @Inject
    public QueryResource(RestHighLevelClient restHighLevelClient) {

        this.elasticClient = requireNonNull(restHighLevelClient);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response query(@QueryParam("message") String message, @QueryParam("header") String header) {
        try {
            SearchRequest searchRequest = buildQuery(message, header);
            String hits = getHits(searchRequest);
            return Response.ok().entity(hits).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private String getHits(SearchRequest searchRequest) throws IOException, JSONException {
        SearchResponse searchResponse = elasticClient.search(searchRequest);
        return searchResponse.toString();
    }

    private SearchRequest buildQuery(String message, String header) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder qb = new BoolQueryBuilder();
        qb.must(QueryBuilders.matchQuery("message", message));
        qb.must(QueryBuilders.matchQuery("header", header));
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
