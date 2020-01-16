package jettyServer.resources.queryResource;

import accounts.AccountsClient;
import accounts.pojos.AccountData;
import common.api.RequestConstants;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Path("search")
public class QueryResource {

    private final RestHighLevelClient elasticClient;
    private final AccountsClient accountsClient;

    @Inject
    public QueryResource(RestHighLevelClient restHighLevelClient, AccountsClient accountsClient) {
        this.elasticClient = requireNonNull(restHighLevelClient);
        this.accountsClient = requireNonNull(accountsClient);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response query(@Context HttpHeaders httpHeaders, @QueryParam("message") String message, @QueryParam("header") String header) {

        String accountToken = httpHeaders.getHeaderString(RequestConstants.ACCOUNT_TOKEN_KEY);


        Optional<AccountData> optionalAccountData = accountsClient.getAccount(accountToken);
        if (optionalAccountData.isPresent()) {
            AccountData accountData = optionalAccountData.get();
            SearchRequest searchRequest = buildQuery(accountData.getIndexName(), message, header);
            SearchHit[] hits = getHits(searchRequest);
            List<Map<String,Object>> responseBody = generateSearchResponse(hits);
            return Response.ok().entity(responseBody).build();
        } else {
            throw new NotAuthorizedException("Token not authorized");
        }

    }

    private SearchHit[] getHits(SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = elasticClient.search(searchRequest);
            return searchResponse.getHits().getHits();
        } catch (IOException ioe) {
            throw new RuntimeException((ioe));
        }
    }

    private List<Map<String,Object>> generateSearchResponse(SearchHit[] searchHits){
        List<Map<String,Object>> res = new ArrayList<>();

        for (SearchHit hit : searchHits){
            res.add(hit.getSourceAsMap());
        }
        return res;
    }

    private SearchRequest buildQuery(String index, String message, String header) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder qb = new BoolQueryBuilder();
        qb.must(QueryBuilders.matchQuery("message", message));
        qb.must(QueryBuilders.matchQuery("header", header));
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
