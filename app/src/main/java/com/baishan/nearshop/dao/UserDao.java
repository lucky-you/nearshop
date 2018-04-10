package com.baishan.nearshop.dao;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.easemob.EaseMobHelper;
import com.baishan.nearshop.model.ChatUser;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.utils.ConstantValue;
import com.igexin.sdk.PushManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class UserDao {

    public static void login(UserInfo user) {
        SPUtils.set(ConstantValue.SP_USER_TOKEN, user.LoginToken);
        BaseApplication.getInstance().setUserInfo(user);
        EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_UPDATE_USER));
        PushManager.getInstance().bindAlias(BaseApplication.getInstance(), user.UserId + "");
        EaseMobHelper.getInstance().login(user.UserId + "", user.UserPwd, user.NickName);
        ChatUser chatUser = new ChatUser();
        chatUser.setUserID(user.UserId);
        chatUser.setName(user.NickName);
        chatUser.setPortrait(user.UserPhoto);
        DBManager.getInstance().getChatUserDao().insertOrReplace(chatUser);
    }

    public static void logout() {
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            PushManager.getInstance().unBindAlias(BaseApplication.getInstance(), userInfo.UserId + "", true);
        }
        EaseMobHelper.getInstance().logout(true, null);
        BaseApplication.getInstance().setUserInfo(null);
        BaseApplication.getInstance().getCurrentArea().defaultAddress = null;
        SPUtils.set(ConstantValue.SP_USER_TOKEN, "");
        EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_UPDATE_USER));
    }

}
