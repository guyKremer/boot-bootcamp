package accounts.pojos;

public class AccountData {

    private String name;
    private String indexName;
    private long id;
    private String token;

    public AccountData() {
    }

    public AccountData(String accountName, String indexName, String token) {
        this.name = accountName;
        this.indexName = indexName;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public long getId() {
        return id;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getName() {
        return name;
    }

}

