package com.baishan.nearshop.presenter;

import android.support.v4.widget.SwipeRefreshLayout;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.ShopOrder;
import com.baishan.nearshop.view.IGoodsOrderView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class GoodsOrderPresenter extends BasePresenter<IGoodsOrderView>{
    public GoodsOrderPresenter(IGoodsOrderView mvpView) {
        super(mvpView);
    }

    public void getShopOrderList(String type, int userId, int pageNow,SwipeRefreshLayout srl) {
        addSubscription(srl,AppClient.getApiService().getShopOrderList(type, userId, pageNow), new SubscriberCallBack<List<ShopOrder>>() {
            @Override
            protected void onSuccess(List<ShopOrder> response) {
                mvpView.getShopOrderListSuccess(response);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mvpView.stopRefresh();
            }
        });
    }
}
