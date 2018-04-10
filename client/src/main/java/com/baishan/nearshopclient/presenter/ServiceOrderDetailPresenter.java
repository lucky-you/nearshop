package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.view.IServiceOrderDetailView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class ServiceOrderDetailPresenter extends BasePresenter<IServiceOrderDetailView> {

    public ServiceOrderDetailPresenter(IServiceOrderDetailView mvpView) {
        super(mvpView);
    }

    public void getOrderInfo(String orderNo) {
        addSubscription(AppClient.getApiService().getServiceOrderInfo(orderNo), new SubscriberCallBack<List<Orders>>() {
            @Override
            protected void onSuccess(List<Orders> response) {
                if(response.size()>0){
                    mvpView.getOrderInfoSuccess(response.get(0));
                }
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

}
