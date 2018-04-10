package com.baishan.nearshopclient.presenter;


import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.Goods;
import com.baishan.nearshopclient.model.ShopStore;
import com.baishan.nearshopclient.view.IGoodsDetailView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView> {
    public GoodsDetailPresenter(IGoodsDetailView mvpView) {
        super(mvpView);
    }

    public void getShopStoreList( int areaId, int productId){
        addSubscription(AppClient.getApiService().getShopStoreList(areaId, productId), new SubscriberCallBack<List<ShopStore>>() {
            @Override
            protected void onSuccess(List<ShopStore> response) {
                mvpView.getShopStoreListSuccess(response);
            }
        });
    }

    public void  getGoodsInfo(int id){
        addSubscription(AppClient.getApiService().getGoodsInfo(id), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                if(response.size()>0){
                    mvpView.getGoodsInfoSuccess(response.get(0));
                }
            }
        });
    }
}
