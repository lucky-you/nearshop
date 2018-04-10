package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.model.ServiceOrders;
import com.baishan.nearshopclient.model.ShopOrder;
import com.baishan.nearshopclient.view.IOrdersView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class OrdersPresenter extends BasePresenter<IOrdersView> {

    private int areaId;

    public OrdersPresenter(IOrdersView mvpView) {
        super(mvpView);
        areaId = BaseApplication.getInstance().getUserInfo().AreaId;
    }

    public void getServiceOrderList(String type, int id, int pageNow) {
        addSubscription(AppClient.getApiService().getServiceOrderList(type, id, pageNow, areaId), new SubscriberCallBack<List<ServiceOrders>>() {
            @Override
            protected void onSuccess(List<ServiceOrders> response) {
                mvpView.getServiceOrderListSuccess(response);
            }
        });
    }

    public void changeState(String method, String orderNo, int id) {
        addSubscription(true, AppClient.getApiService().changeState(method, orderNo, id + ""), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(response, orderNo);
            }
        });
    }

    public void changeState(String method, String orderNo, int id, int orderType) {
        addSubscription(true, AppClient.getApiService().changeState(method, orderNo, id + "", orderType), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeStateSuccess(method, orderNo);
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
                mvpView.changeStateSuccess(method, orderNo);
            }
        });
    }

    public void getSenderShopOrderList(String type, int id, int pageNow) {
        addSubscription(AppClient.getApiService().getSenderShopOrderList(type, id, pageNow, areaId), new SubscriberCallBack<List<SenderGoodsOrders>>() {
            @Override
            protected void onSuccess(List<SenderGoodsOrders> response) {
                mvpView.getSenderShopOrderListSuccess(response);
            }
        });
    }

    public void getAdminShopOrderList(String type, int id, int pageNow) {
        addSubscription(AppClient.getApiService().getAdminShopOrderList(type, id, pageNow, areaId), new SubscriberCallBack<List<ShopOrder>>() {
            @Override
            protected void onSuccess(List<ShopOrder> response) {
                mvpView.getShopOrderListSuccess(response);
            }
        });
    }

    public void getOrderList(String type, int id, int pageNow) {
        addSubscription(AppClient.getApiService().getOrderList(type, id, pageNow, areaId), new SubscriberCallBack<List<ShopOrder>>() {
            @Override
            protected void onSuccess(List<ShopOrder> response) {
                mvpView.getShopOrderListSuccess(response);
            }
        });
    }

    public void getAdminServiceOrderList(String type, int id, int pageNow) {
        addSubscription(AppClient.getApiService().getAdminServiceOrderList(type, id, pageNow, areaId), new SubscriberCallBack<List<Orders>>() {
            @Override
            protected void onSuccess(List<Orders> response) {
                mvpView.getAdminServiceOrderListSuccess(response);
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

    //退款确认
    public void ConfirmSupplyTk(String orderNo, int id) {
        addSubscription(AppClient.getApiService().confirmSupply("ConfirmSupplyTk", orderNo, id), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.confirmSupplySuccess();
            }
        });
    }
}
