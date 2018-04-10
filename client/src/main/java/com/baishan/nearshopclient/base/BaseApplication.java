package com.baishan.nearshopclient.base;


import com.baishan.mylibrary.LibApplication;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.service.PushIntentService;
import com.baishan.nearshopclient.service.PushService;
import com.igexin.sdk.PushManager;

/**
 * Created by RayYeung on 2016/8/8.
 */
public class BaseApplication extends LibApplication {
    private UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.init("NearShopClient");
        PushManager.getInstance().initialize(getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), PushIntentService.class);
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
}
