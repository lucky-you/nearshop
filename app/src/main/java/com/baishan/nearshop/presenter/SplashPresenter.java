package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.dao.UserDao;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.view.ISplashView;

import java.util.List;

/**
 * Created by RayYeung on 2016/10/18.
 */

public class SplashPresenter extends BasePresenter<ISplashView> {

    public SplashPresenter(ISplashView mvpView) {
        super(mvpView);
    }

    public void quietLogin(String token) {

        addSubscription(AppClient.getApiService().quietLogin(token), new SubscriberCallBack<List<UserInfo>>() {
            @Override
            protected void onSuccess(List<UserInfo> response) {
                if (response.size() > 0) {
                    UserDao.login(response.get(0));
                }else{
                    mvpView.onServerError();
                }
            }

            @Override
            protected void onError() {
                super.onError();
                mvpView.onServerError();
            }

            @Override
            protected void onFailure(ResultResponse response) {
                super.onFailure(response);
                mvpView.onLoginFailure(response);
            }
        });
    }

    public void getUserCurrentArea(String adCode, double longitude, double latitude) {
        addSubscription(AppClient.getApiService().getUserCurrentArea(adCode, longitude, latitude), new SubscriberCallBack<Area>() {
            @Override
            protected void onSuccess(Area response) {
                if(response!=null){
                    mvpView.getUserCurrentAreaSuccess(response);
                }else{
                    mvpView.getUserCurrentAreaFailure();
                }
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mvpView.getUserCurrentAreaFailure();
            }

            @Override
            protected void onError() {
                mvpView.getUserCurrentAreaFailure();
            }
        });
    }

    public void getAreaInfo(int areaId){
        addSubscription(AppClient.getApiService().getAreaInfo(areaId), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                if(response.size()>0){
                    mvpView.getAreaInfoSuccess(response.get(0));
                }else{
                    mvpView.getAreaInfoFail(areaId);
                }
            }
            @Override
            protected void onFailure(ResultResponse response) {
                mvpView.getAreaInfoFail(areaId);
            }

            @Override
            protected void onError() {
                mvpView.getAreaInfoFail(areaId);
            }
        });
    }
}
