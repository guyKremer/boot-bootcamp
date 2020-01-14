package jettyServer.resources.queryResource;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
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
import exceptions.DbAccessException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;

import static java.util.Objects.requireNonNull;


import java.io.IOException;
import java.util.Optional;

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

        String accountToken = httpHeaders.getHeaderString("X-ACCOUNT-TOKEN");


        try {
            Optional<AccountData> optionalAccountData = accountsClient.getAccount(accountToken);
            if(optionalAccountData.isPresent()){
                AccountData accountData = optionalAccountData.get();
                SearchRequest searchRequest = buildQuery(accountData, message, header);
                String hits = getHits(searchRequest);
                return Response.ok().entity(hits).build();
            }
            else{
                throw new NotAuthorizedException("Token not authorized");
            }
        }
        catch (IOException | DbAccessException dbe){
            throw new InternalServerErrorException();
        }

    }

    private String getHits(SearchRequest searchRequest) throws IOException  {
        SearchResponse searchResponse = elasticClient.search(searchRequest);
        return searchResponse.toString();
    }

    private SearchRequest buildQuery(AccountData account, String message, String header) {
        String indexName = account.getIndexName();
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder qb = new BoolQueryBuilder();
        qb.must(QueryBuilders.matchQuery("message", message));
        qb.must(QueryBuilders.matchQuery("header", header));
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
