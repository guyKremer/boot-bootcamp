package test;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class Test {
    private RestHighLevelClient elasticClient;

    public Test(){
        elasticClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("elasticSearch", 9200, "http")));
    }

    public void run(){

    }
}
