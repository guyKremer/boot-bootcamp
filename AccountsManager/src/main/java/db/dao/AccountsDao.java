package db.dao;

import javax.inject.Inject;

import accounts.pojos.AccountData;
import db.mappers.AccountsMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.guice.transactional.Transactional;

public class AccountsDao {


    @Inject
    private AccountsMapper accountsMapper;

    @Transactional
    public AccountData selectAccount(String token) {
        return accountsMapper.selectAccount(token);
    }

    @Transactional
    public void insertAccount(AccountData accountData) {

        accountsMapper.insertAccount(accountData);
    }

}
