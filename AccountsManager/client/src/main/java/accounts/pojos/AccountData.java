package accounts.pojos;

import org.apache.commons.lang3.RandomStringUtils;

public class AccountData {

    private String name;
    private String indexName;
    private long id;
    private String token;

    public AccountData() {
    }

    public AccountData(String accountName) {
        setAccount(accountName);
    }

    private void setAccount(String accountName) {
        String indexName = RandomStringUtils.random(33, true, false).toLowerCase();
        String token = RandomStringUtils.random(33, true, false);

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

