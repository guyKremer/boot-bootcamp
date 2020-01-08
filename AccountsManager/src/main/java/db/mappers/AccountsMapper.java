package db.mappers;

import pojos.AccountData;

public interface AccountsMapper {

     AccountData selectAccount(String token);

     int insertAccount(AccountData accountData);
}
