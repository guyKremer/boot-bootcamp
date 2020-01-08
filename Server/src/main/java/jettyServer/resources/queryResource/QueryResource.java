package jettyServer.resources.queryResource;

import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;

import java.io.IOException;

@Path("search")
public class QueryResource {

    private final RestHighLevelClient elasticClient;
    private final AccountsClient accountsClient;

    @Inject
    public QueryResource(RestHighLevelClient restHighLevelClient,AccountsClient accountsClient) {
        this.elasticClient = restHighLevelClient;
        this.accountsClient = accountsClient;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response query(@Context HttpHeaders httpHeaders, @QueryParam("message") String message, @QueryParam("header") String header) {

        String accountToken = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");
        boolean authenticated = accountsClient.isAuthenticated(accountToken);

        if(! authenticated){
            throw new NotAuthorizedException("Token not authorized");
        }

        try {
            SearchRequest searchRequest = buildQuery(accountToken,message,header);
            String hits = getHits(searchRequest);
            return Response.ok().entity(hits).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private String getHits(SearchRequest searchRequest) throws IOException, JSONException {
        SearchResponse searchResponse = elasticClient.search(searchRequest);
        return searchResponse.toString();
    }
    private SearchRequest buildQuery(String accountToken,String message,String header){
        AccountData account = accountsClient.getAccount(accountToken);
        String indexName = account.getEsIndexName();
        System.out.println(indexName);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder qb =  new BoolQueryBuilder();
        qb.must(QueryBuilders.matchQuery("message", message));
        qb.must(QueryBuilders.matchQuery("header",header));
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
