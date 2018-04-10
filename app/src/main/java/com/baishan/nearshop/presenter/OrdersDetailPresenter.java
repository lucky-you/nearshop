package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.view.IOrdersDetailView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class OrdersDetailPresenter extends BasePresenter<IOrdersDetailView> {
    public OrdersDetailPresenter(IOrdersDetailView mvpView) {
        super(mvpView);
    }

    public void getGoodsOrderInfo( String orderNo){
        addSubscription(AppClient.getApiService().getGoodsOrderInfo(orderNo), new SubscriberCallBack<List<ConfirmOrderItem>>() {
            @Override
            protected void onSuccess(List<ConfirmOrderItem> response) {
                if(response.size()>0){
                    mvpView.getGoodsOrderInfoSuccess(response.get(0));
                }
            }
        });
    }
}
