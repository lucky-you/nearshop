package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.view.ISearchOrdersView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public class SearchOrdersPresenter extends BasePresenter<ISearchOrdersView> {
    public SearchOrdersPresenter(ISearchOrdersView mvpView) {
        super(mvpView);
    }

    public void searchKeywords(int id, String keywords, int pageNow) {
        addSubscription(AppClient.getApiService().searchOrder(id + "", keywords, pageNow), new SubscriberCallBack<List<SenderGoodsOrders>>() {
            @Override
            protected void onSuccess(List<SenderGoodsOrders> response) {
                mvpView.searchKeywordsSuccess(response);
            }
        });
    }

    public void changeState(String method, String orderNo, int id) {
        addSubscription(AppClient.getApiService().changeState(method, orderNo, id + ""), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(response);
            }
        });
    }

    public void changeState(String method, String orderNo, int id, int orderType) {
        addSubscription(AppClient.getApiService().changeState(method, orderNo, id + "", orderType), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(response);
            }
        });
    }

    public void finishWork(String method, String orderNo, int id, String remark) {

        Map<String, String> parmas = new HashMap<>();
        parmas.put("Method", method);
        parmas.put("OrderNo", orderNo);
        parmas.put("CourierId", id + "");
        parmas.put("CourierRemarks", remark);
        addSubscription(true, AppClient.getApiService().finishWork(parmas), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(response);
            }
        });
    }
}
