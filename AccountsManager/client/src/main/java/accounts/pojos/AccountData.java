package accounts.pojos;

public class AccountData {

    private String name;
    private String esIndexName;
    private long id;
    private String accountToken;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEsIndexName() {
        return esIndexName;
    }

    public void setIndexName(String indexName) {
        this.esIndexName = indexName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }
}
