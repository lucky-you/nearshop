package com.baishan.nearshopclient.presenter;


import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.dao.PushParserDao;
import com.baishan.nearshopclient.db.DBManager;
import com.baishan.nearshopclient.model.PushParser;
import com.baishan.nearshopclient.view.IMessageView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class MessagePresenter extends BasePresenter<IMessageView> {

    public MessagePresenter(IMessageView mvpView) {
        super(mvpView);
    }

    public List<PushParser> getMessage(String userId) {
        QueryBuilder qb = DBManager.getInstance().getPushDao().queryBuilder();
        return (List<PushParser>) qb.where(PushParserDao.Properties.UserId.eq(userId)).orderDesc(PushParserDao.Properties.PushTime).list();
    }
}
