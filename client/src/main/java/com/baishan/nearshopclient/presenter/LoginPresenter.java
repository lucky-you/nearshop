package com.baishan.nearshopclient.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.view.ILoginView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private static final String TAG = "LoginPresenter";

    public LoginPresenter(ILoginView mvpView) {
        super(mvpView);
    }


    public void Login(String phone, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast("请输入密码");
            return;
        }

        addSubscription(true, AppClient.getApiService().login("LoginOther", phone, pwd), new SubscriberCallBack<List<UserInfo>>() {
            @Override
            protected void onSuccess(List<UserInfo> response) {
                Log.d(TAG, "onSuccess: response==" + response.toArray().toString());
                mvpView.onLoginSuccess(response, phone, pwd);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
            }
        });

//        UserInfo user = new UserInfo();
//        user.setIdentity(Integer.parseInt(phone));
//        mvpView.success(user);
    }
}
