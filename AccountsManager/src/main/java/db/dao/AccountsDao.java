package db.dao;

import javax.inject.Inject;

import accounts.pojos.AccountData;
import db.mappers.AccountsMapper;
import exceptions.DbAccessException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.guice.transactional.Transactional;

public class AccountsDao {

    Logger logger = LogManager.getLogger(AccountsDao.class);


    @Inject
    private AccountsMapper accountsMapper;

    @Transactional
    public AccountData selectAccount(String token) throws DbAccessException {
        try{
            return accountsMapper.selectAccount(token);
        }
        catch (PersistenceException pe){
            logger.debug(pe.getCause());
            throw new DbAccessException (pe.getMessage());
        }
    }

    @Transactional
    public void insertAccount(AccountData accountData) throws DbAccessException {
        try{
            accountsMapper.insertAccount(accountData);
        }
        catch (PersistenceException pe){
            logger.debug(pe.getCause());
            throw new DbAccessException (pe.getMessage());
        }
    }
}
