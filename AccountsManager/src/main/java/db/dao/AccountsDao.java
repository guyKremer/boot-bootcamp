package db.dao;

import javax.inject.Inject;

import db.mappers.AccountsMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.guice.transactional.Transactional;
import pojos.AccountData;

public class AccountsDao {

    @Inject
    private AccountsMapper accountsMapper;

    @Transactional
    public AccountData selectAccount(String token){
        return accountsMapper.selectAccount(token);
    }

    @Transactional
    public void insertAccount(AccountData accountData) throws PersistenceException{
        try{
            accountsMapper.insertAccount(accountData);
        }
        catch (PersistenceException e){
            throw e;
        }
    }
}
