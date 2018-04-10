package com.baishan.nearshopclient.dao;

import com.baishan.nearshopclient.db.DBManager;
import com.baishan.nearshopclient.model.PushParser;
import com.baishan.nearshopclient.model.PushValue;

import java.util.List;

/**
 * Created by RayYeung on 2016/12/6.
 */

public class MessageDao {


    public static void read(String orderNo) {
        List<PushValue> values = DBManager.getInstance().getPushValueDao().queryBuilder().where(PushValueDao.Properties.OrderNo.eq(orderNo)).list();
        if (values.size() > 0) {
            PushParserDao dao = DBManager.getInstance().getPushDao();
            for (PushValue value : values) {
                List<PushParser> list = dao.queryBuilder().where(PushParserDao.Properties.ValueId.eq(value.getId())).list();
                for (PushParser parser : list) {
                    if (!parser.getIsRead()) {
                        parser.setIsRead(true);
                        parser.update();
                    }
                }
            }
        }
    }

    public static int getUnReadMsgCount(String userId) {
        return (int) DBManager.getInstance().getPushDao()
                .queryBuilder()
                .where(PushParserDao.Properties.UserId.eq(userId), PushParserDao.Properties.IsRead.eq(false)).count();
    }
}
