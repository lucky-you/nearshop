package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Banner;
import com.baishan.nearshop.model.FirstProduct;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.model.HotNews;
import com.baishan.nearshop.view.IHomeView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class HomePresenter extends BasePresenter<IHomeView> {
    public HomePresenter(IHomeView mvpView) {
        super(mvpView);
    }

    public void getBanner(Map<String,Object> params) {
        addSubscription(AppClient.getApiService().getHomeBanner(params), new SubscriberCallBack<List<Banner>>() {

            @Override
            protected void onSuccess(List<Banner> response) {
                if (response.size() > 0) {
                    mvpView.getBannerSuccess(response);
                }
            }
        });
    }


    public void getFastService(Map<String,Object> params) {
        addSubscription(AppClient.getApiService().getFastService(params), new SubscriberCallBack<List<GridType>>() {
            @Override
            protected void onSuccess(List<GridType> response) {
                if (response.size() > 0) {
                    mvpView.getFastServiceSuccess(response);
                }
            }
        });
    }


    public void getHotNews() {
        addSubscription(AppClient.getApiService().getHotNews(), new SubscriberCallBack<List<HotNews>>() {
            @Override
            protected void onSuccess(List<HotNews> response) {
                mvpView.onHotNewsSuccess(response);
            }
        });
    }

    public void getFirstProducts(int areaId) {
        addSubscription(AppClient.getApiService().getFirstProducts(areaId), new SubscriberCallBack<List<FirstProduct>>() {
            @Override
            protected void onSuccess(List<FirstProduct> response) {
                mvpView.onFirstProductSuccess(response);
            }
        });
    }

    public void getHotShopList(int areaId,int pageNow){
        addSubscription(AppClient.getApiService().getHotShopList(areaId, pageNow), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                mvpView.getHotShopListSuccess(response);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mvpView.stopRefresh();
            }
        });
    }


}
