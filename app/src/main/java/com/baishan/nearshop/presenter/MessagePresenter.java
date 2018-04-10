package com.baishan.nearshop.presenter;

import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.dao.PushParserDao;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.PushParser;
import com.baishan.nearshop.view.IMessageView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class MessagePresenter extends BasePresenter<IMessageView> {

    public MessagePresenter(IMessageView mvpView) {
        super(mvpView);
    }

    public List<PushParser> getAllMessage(int type, String loginToken) {
        DBManager.getInstance().getDaoSession().clear();
        QueryBuilder<PushParser> builder = DBManager.getInstance().getPushDao().queryBuilder();
        if (type == 1) {
            builder.where(PushParserDao.Properties.Type.eq(type));
        } else if (TextUtils.isEmpty(loginToken)) {
            return new ArrayList<>();
        } else {
            builder.where(PushParserDao.Properties.Type.eq(type), PushParserDao.Properties.LoginToken.eq(loginToken));
        }
        return builder.orderDesc(PushParserDao.Properties.PushTime).list();
    }
}
