package pojos;


import org.apache.commons.lang3.RandomStringUtils;

public class AccountData {

    private String name;
    private String indexName;
    private long id;
    private String token;

    public AccountData(){}

    public AccountData (String accountName){
        setAccount(accountName);
    }

    private void setAccount(String accountName) {
        AccountData accountData = new AccountData();
        String indexName = RandomStringUtils.random(33,true,false).toLowerCase();
        String token = RandomStringUtils.random(33,true,false);

        this.name = name;
        this.indexName = indexName;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountToken() {
        return token;
    }

    public void setAccountToken(String accountToken) {
        this.token = accountToken;
    }
}
