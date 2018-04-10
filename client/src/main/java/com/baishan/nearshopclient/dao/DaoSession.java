package com.baishan.nearshopclient.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.baishan.nearshopclient.model.Account;
import com.baishan.nearshopclient.model.PushParser;
import com.baishan.nearshopclient.model.PushValue;

import com.baishan.nearshopclient.dao.AccountDao;
import com.baishan.nearshopclient.dao.PushParserDao;
import com.baishan.nearshopclient.dao.PushValueDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig pushParserDaoConfig;
    private final DaoConfig pushValueDaoConfig;

    private final AccountDao accountDao;
    private final PushParserDao pushParserDao;
    private final PushValueDao pushValueDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        pushParserDaoConfig = daoConfigMap.get(PushParserDao.class).clone();
        pushParserDaoConfig.initIdentityScope(type);

        pushValueDaoConfig = daoConfigMap.get(PushValueDao.class).clone();
        pushValueDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        pushParserDao = new PushParserDao(pushParserDaoConfig, this);
        pushValueDao = new PushValueDao(pushValueDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(PushParser.class, pushParserDao);
        registerDao(PushValue.class, pushValueDao);
    }
    
    public void clear() {
        accountDaoConfig.clearIdentityScope();
        pushParserDaoConfig.clearIdentityScope();
        pushValueDaoConfig.clearIdentityScope();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public PushParserDao getPushParserDao() {
        return pushParserDao;
    }

    public PushValueDao getPushValueDao() {
        return pushValueDao;
    }

}