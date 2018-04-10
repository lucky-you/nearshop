package com.baishan.nearshop.presenter;

import android.support.v4.widget.SwipeRefreshLayout;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Orders;
import com.baishan.nearshop.view.IEasyOrdersView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class EasyOrdersPresenter extends BasePresenter<IEasyOrdersView> {
    public EasyOrdersPresenter(IEasyOrdersView mvpView) {
        super(mvpView);
    }

    public void getServiceOrderList(String type, int userId, int pageNow,SwipeRefreshLayout srl) {
        addSubscription(srl,AppClient.getApiService().getServiceOrderList(type, userId, pageNow), new SubscriberCallBack<List<Orders>>() {
            @Override
            protected void onSuccess(List<Orders> response) {
                mvpView.getServiceOrderListSuccess(response);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mvpView.stopRefresh();
            }
        });
    }


}
