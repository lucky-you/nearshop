package com.baishan.nearshop.presenter;

import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.dao.UserDao;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.view.ILoginView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class LoginPresenter extends BasePresenter<ILoginView>{

    public LoginPresenter(ILoginView mvpView) {
        super(mvpView);
    }


    public void login(String phone, String pwd) {
        if (!CommonUtil.checkPhone(phone)) {
            mvpView.onPhoneError();
            return;
        }
        if (TextUtils.isEmpty(pwd)||pwd.length()<6) {
            mvpView.onPassWordError();
            return;
        }
        addSubscription(true,AppClient.getApiService().login("Login", phone, pwd), new SubscriberCallBack<List<UserInfo>>() {
            @Override
            protected void onSuccess(List<UserInfo> response) {
                if (response.size() > 0) {
                    UserDao.login(response.get(0));
                }
            }

        });
    }


}
