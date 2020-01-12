package db.mappers;


import accounts.pojos.AccountData;

public interface AccountsMapper {

     AccountData selectAccount(String token);

     int insertAccount(AccountData accountData);
}
