package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.ShopStore;
import com.baishan.nearshop.view.IGoodsDetailView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView> {
    public GoodsDetailPresenter(IGoodsDetailView mvpView) {
        super(mvpView);
    }


    public void getShopStoreList(int areaId, int productId) {
        addSubscription(AppClient.getApiService().getShopStoreList(areaId, productId), new SubscriberCallBack<List<ShopStore>>() {
            @Override
            protected void onSuccess(List<ShopStore> response) {
                mvpView.getShopStoreListSuccess(response);
            }
        });
    }

    public void getGoodsInfo(int id) {
        addSubscription(true, AppClient.getApiService().getGoodsInfo(id), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                if (response.size() > 0) {
                    mvpView.getGoodsInfoSuccess(response.get(0));
                }
            }
        });
    }

    public void getAreaInfo(int areaId) {
        addSubscription(true, AppClient.getApiService().getAreaInfo(areaId), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                if (response.size() > 0) {
                    mvpView.getAreaInfoSuccess(response.get(0));
                } else {
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
