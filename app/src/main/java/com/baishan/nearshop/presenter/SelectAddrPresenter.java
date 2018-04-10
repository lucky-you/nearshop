package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.City;
import com.baishan.nearshop.view.ISelectAddrView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/22.
 */
public class SelectAddrPresenter extends BasePresenter<ISelectAddrView> {

    public SelectAddrPresenter(ISelectAddrView mvpView) {
        super(mvpView);
    }



    public void getAreaList(String cityCode){
        addSubscription(AppClient.getApiService().getAreaList(cityCode), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                mvpView.getAreaListSuccess(response);
            }
        });
    }

    public void getShopAreaList(String adCode){
        addSubscription(AppClient.getApiService().getShopAreaList(adCode), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                mvpView.getShopAreaListSuccess(response);
            }
        });
    }

    public void getHotCities(){
        addSubscription(AppClient.getApiService().getHotCities(), new SubscriberCallBack<List<City>>() {
            @Override
            protected void onSuccess(List<City> response) {
                mvpView.getHotCitiesSuccess(response);
            }
        });
    }

    public void searchArea(String key){
        addSubscription(AppClient.getApiService().searchArea(key), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                mvpView.searchAreaSuccess(response);
            }
        });
    }

}
