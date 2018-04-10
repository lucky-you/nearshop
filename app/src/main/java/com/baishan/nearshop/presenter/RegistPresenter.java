package com.baishan.nearshop.presenter;

import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.dao.UserDao;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.view.IRegistView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class RegistPresenter extends BasePresenter<IRegistView> {
    public RegistPresenter(IRegistView mvpView) {
        super(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();


    }

    public void regist(String phone, String password, String code) {
        if (!CommonUtil.checkPhone(phone)) {
            mvpView.onPhoneError();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            mvpView.onPassWordError();
            return;
        }
        addSubscription(true,AppClient.getApiService().regist("RegUser", phone, password, code), new SubscriberCallBack<List<UserInfo>>() {
            @Override
            protected void onSuccess(List<UserInfo> response) {
                if (response.size() > 0) {
                    UserDao.login(response.get(0));
                }
            }

            @Override
            protected void onFailure(ResultResponse response) {
                super.onFailure(response);
                if(response.Code==304){
                    ToastUtils.showToast("该账号已注册");
                }
            }
        });
    }

    public void changePwd(String phone, String password, String code) {
        if (!CommonUtil.checkPhone(phone)) {
            mvpView.onPhoneError();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            mvpView.onPassWordError();
            return;
        }
        addSubscription(true,AppClient.getApiService().changePassword("ResetPassword", phone, password, code), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                UserDao.logout();
                mvpView.onChangeSuccess();
            }

        });
    }


}
