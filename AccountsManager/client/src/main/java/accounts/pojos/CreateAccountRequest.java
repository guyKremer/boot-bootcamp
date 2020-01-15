package accounts.pojos;

public class CreateAccountRequest {

    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String accountName) {
        this.accountName = accountName;
    }

}
