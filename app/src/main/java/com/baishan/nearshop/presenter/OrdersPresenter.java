package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.IOrdersView;

/**
 * Created by RayYeung on 2016/11/28.
 */

public class OrdersPresenter extends BasePresenter<IOrdersView> {

    public OrdersPresenter(IOrdersView mvpView) {
        super(mvpView);
    }


    public void cancelOrders(String orderNo, int userId) {
        ordersOperate("UserCancelOrder", orderNo, userId);
    }

    public void cancelGoodsOrders(String orderNo, int userId) {
        ordersOperate("CancelShopOrder", orderNo, userId);
    }

    public void confirmOrders(String orderNo, int userId) {
        ordersOperate("ConfirmOk", orderNo, userId);
    }

    public void confirmGoodsOrders(String orderNo, int userId) {
        ordersOperate("ConfirmOkShopOrder", orderNo, userId);
    }

    public void applyRefund(String orderNo, int userId) {
        ordersOperate("ApplyRefund", orderNo, userId);
    }

    public void applyGoodsRefund(String orderNo, int userId) {
        ordersOperate("ApplyShopRefund", orderNo, userId);
    }


    private void ordersOperate(String method, String orderNo, int userId) {
        addSubscription(true,AppClient.getApiService().ordersOperate(method, orderNo, userId), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                switch (method) {
                    case "UserCancelOrder":
                    case "CancelShopOrder":
                        mvpView.cancelOrdersSuccess();
                        break;
                    case "ConfirmOk":
                    case "ConfirmOkShopOrder":
                        mvpView.confirmOrdersSuccess();
                        break;
                    case "ApplyRefund":
                    case "ApplyShopRefund":
                        mvpView.applyRefundSuccess();
                        break;
                }
            }
        });
    }
}
