package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Orders;
import com.baishan.nearshop.view.IServiceOrderDetailView;

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

}
