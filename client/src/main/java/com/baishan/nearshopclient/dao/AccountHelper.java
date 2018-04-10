package com.baishan.nearshopclient.dao;

import com.baishan.nearshopclient.db.DBManager;
import com.baishan.nearshopclient.model.Account;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10 0010.
 */

public class AccountHelper {
    public static void saveAccount(Account account) {
        DBManager.getInstance().getDaoSession().clear();
        List<Account> accounts = queryList(AccountDao.Properties.Phone.eq(account.Phone));
        if (accounts.size() > 0) {
            //有数据 ，修改
            Account old = accounts.get(0);
            old.setLoginTime(System.currentTimeMillis() + "");
            old.setPhone(account.Phone);
            old.setPassword(account.Password);
            DBManager.getInstance().getAccountDao().update(old);
        } else {
            DBManager.getInstance().getAccountDao().insert(account);
        }
    }

    public static List<Account> getAccountList() {
        return queryList(null);
    }

    private static List<Account> queryList(WhereCondition condition) {
        QueryBuilder<Account> queryBuilder = DBManager.getInstance().getAccountDao().queryBuilder().orderDesc(AccountDao.Properties.LoginTime);
        if (condition != null) queryBuilder.where(condition);
        return queryBuilder.list();
    }

}
