package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.IMeView;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class MePresenter extends BasePresenter<IMeView> {
    public MePresenter(IMeView mvpView) {
        super(mvpView);
    }

    public void getLastCoins( int areaId, String loginToken) {
        addSubscription(AppClient.getApiService().getLastCoins(areaId, loginToken), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.getLastCoinsSuccess(response);
            }

        });
    }

    public void getLastBalance(String loginToken) {
        addSubscription(AppClient.getApiService().getLastBalance( loginToken), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.getLastBalanceSuccess(response);
            }

        });
    }





}
