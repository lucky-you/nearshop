package com.baishan.nearshop.base;

import android.support.multidex.MultiDex;

import com.baishan.mylibrary.LibApplication;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.easemob.EaseMobHelper;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.UserInfo;
import com.bugtags.library.Bugtags;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by RayYeung on 2016/8/8.
 */
public class BaseApplication extends LibApplication {

    private UserInfo userInfo;
    private Area currentArea;

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.init("nearShop");
        DBManager.copyDB(this);
        ShareSDK.initSDK(this);
        Bugtags.start("bea16a8955923d72285f85143e42ec27", this, Bugtags.BTGInvocationEventNone);
        EaseMobHelper.getInstance().init(this);
        MultiDex.install(this);
    }


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static BaseApplication getInstance() {
        return (BaseApplication) instance;
    }

    public Area getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(Area currentArea) {
        this.currentArea = currentArea;
    }

    public void clearGlobalData(){
        userInfo = null;
        currentArea = null;
    }

}
