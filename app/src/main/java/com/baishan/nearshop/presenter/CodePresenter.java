package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.ICodeView;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class CodePresenter extends BasePresenter<ICodeView> {
    public CodePresenter(ICodeView mvpView) {
        super(mvpView);
    }

    public void getCode(String phone) {
        if (!CommonUtil.checkPhone(phone)) {
            mvpView.onPhoneError();
            return;
        }
        mvpView.startCountDown();
        addSubscription(AppClient.getApiService().getCode(phone), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                Logger.i(response);
            }
        });
    }
}
