package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.SelectedStore;
import com.baishan.nearshopclient.model.SenderOrdersDetail;
import com.baishan.nearshopclient.view.IGoodsOrderDetailView;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class GoodsOrdersDetailPresenter extends BasePresenter<IGoodsOrderDetailView> {

    public GoodsOrdersDetailPresenter(IGoodsOrderDetailView mvpView) {
        super(mvpView);
    }

    public void getGoodsOrderInfo(String orderNo) {
        addSubscription(AppClient.getApiService().getGoodsOrderInfo(orderNo), new SubscriberCallBack<List<SenderOrdersDetail>>() {
            @Override
            protected void onSuccess(List<SenderOrdersDetail> response) {
                if (response.size() > 0) {
                    mvpView.getGoodsOrderInfoSuccess(response.get(0));
                }
            }
        });
    }

    public void changeState(String method, Map<String, SelectedStore> stores, String orderNo, int id) {
        addSubscription(true, AppClient.getApiService().changeState(method, orderNo, id + "", new Gson().toJson(stores.values())), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(method);
            }
        });
    }

    public void changeState(String method, String orderNo, int id) {
        changeState(method, orderNo, id, 0);
    }

    public void changeState(String method, String orderNo, int id, int type) {
        addSubscription(true, AppClient.getApiService().changeState(method, orderNo, id + "", type), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(method);
            }
        });
    }

    public void reDistribute(String orderNo, int type) {
        addSubscription(AppClient.getApiService().reDistribute("ResetCourier", orderNo, type), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.reDistributeSuccess();
            }
        });
    }

    public void confirmSupply(String orderNo, int id) {
        addSubscription(AppClient.getApiService().confirmSupply("ConfirmSupply", orderNo, id), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.confirmSupplySuccess();
            }
        });
    }
}
