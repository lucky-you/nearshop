package com.baishan.nearshop.dao;

import android.text.TextUtils;

import com.baishan.nearshop.dao.PushParserDao.Properties;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.PushParser;
import com.baishan.nearshop.model.PushValue;
import com.baishan.nearshop.service.PushIntentService;
import com.baishan.nearshop.ui.activity.MainActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by RayYeung on 2016/12/6.
 */

public class MessageDao {

    public static long getUnreadMsgNum(int type, String loginToken) {
        DBManager.getInstance().getDaoSession().clear();
        PushParserDao dao = DBManager.getInstance().getPushDao();
        QueryBuilder<PushParser> queryBuilder = dao.queryBuilder();
        if (TextUtils.isEmpty(loginToken)) {
            queryBuilder.where(Properties.Type.eq(type), Properties.IsRead.eq(false));
        } else {
            queryBuilder.where(Properties.Type.eq(type), Properties.IsRead.eq(false), Properties.LoginToken.eq(loginToken));
        }
        return queryBuilder.count();
    }


    /**
     * 获取所有未读消息
     *
     * @param loginToken
     * @return
     */
    public static long getUnreadMsgNum(String loginToken) {
        DBManager.getInstance().getDaoSession().clear();
        PushParserDao dao = DBManager.getInstance().getPushDao();
        long count = dao.queryBuilder().where(Properties.PushType.eq(PushIntentService.COMMON), Properties.IsRead.eq(false)).count();
        if (TextUtils.isEmpty(loginToken)) {
            return count;
        }
        long count1 = dao.queryBuilder().where(Properties.LoginToken.eq(loginToken), Properties.IsRead.eq(false)).count();
        return count + count1;
    }

    public static void read(String orderNo) {
        DBManager.getInstance().getDaoSession().clear();
        List<PushValue> values = DBManager.getInstance().getPushValueDao().queryBuilder().where(PushValueDao.Properties.OrderNo.eq(orderNo)).list();
        if (values.size() > 0) {
            PushParserDao dao = DBManager.getInstance().getPushDao();
            for (PushValue value : values) {
                List<PushParser> list = dao.queryBuilder().where(Properties.ValueId.eq(value.getId())).list();
                for (PushParser parser : list) {
                    if (!parser.getIsRead()) {
                        parser.setIsRead(true);
                        parser.update();
                    }
                }
            }
            MainActivity.getInstance().setMessage();
        }
    }
}
