package com.baishan.nearshopclient.db;

import android.content.Context;

import com.baishan.nearshopclient.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by RayYeung on 2016/9/6.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

    }
}
