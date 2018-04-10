package com.baishan.nearshopclient.dao;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.model.Account;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.igexin.sdk.PushManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class UserDao {

    public static void login(UserInfo user, String phone, String pwd) {
        SPUtils.set(ConstantValue.SP_IS_LOGIN,true);
        PushManager.getInstance().bindAlias(BaseApplication.getInstance(), user.IdentityFlag + "_" + user.Id);
        AccountHelper.saveAccount(new Account(phone, pwd, System.currentTimeMillis() + ""));
        BaseApplication.getInstance().setUserInfo(user);
        EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_UPDATE_USER));
    }

    public static void logout() {
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null)
            PushManager.getInstance().unBindAlias(BaseApplication.getInstance(), userInfo.IdentityFlag + "_" + userInfo.Id, false);
        BaseApplication.getInstance().setUserInfo(null);
        SPUtils.set(ConstantValue.SP_USER_TOKEN, "");
        EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_UPDATE_USER));
    }

}
