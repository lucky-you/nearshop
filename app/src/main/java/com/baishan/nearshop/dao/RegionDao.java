package com.baishan.nearshop.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class RegionDao {


    /**
     * 获取所有省的数据(排除了港澳台)
     *
     * @return
     */
    public static List<Region> getProvinces() {
        String sql = "select * from region where AreaLevel = 'province' and _id < 7372";
        return query(sql, null);
    }


    /**
     * 查询城市
     *
     * @return
     */
    public static List<Region> queryCities(String adCode) {
        String sql = "select * from region where AdCode like ? and AreaLevel = 'city'";
        return query(sql, new String[]{adCode.substring(0, 2) + "%"});
    }

    /**
     * 查询区
     *
     * @param cityCode
     * @return
     */
    public static List<Region> queryAreas(String cityCode) {
        String sql = "select * from region where CityCode = ? and AreaLevel = 'district'";
        return query(sql, new String[]{cityCode});
    }


    private static List<Region> query(String sql, String[] selectionArgs) {
        List<Region> list = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DBManager.DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Region location = new Region();
                location.CityCode = cursor.getString(1);
                location.AdCode = cursor.getString(2);
                location.Name = cursor.getString(3);
                list.add(location);
            }
            cursor.close();
        }
        db.close();
        return list;
    }


}
