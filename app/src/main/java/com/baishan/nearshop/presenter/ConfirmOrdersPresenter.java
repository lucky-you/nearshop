package com.baishan.nearshop.presenter;

import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.model.OrdersParam;
import com.baishan.nearshop.utils.AlipayUtils;
import com.baishan.nearshop.view.IConfirmOrdersView;
import com.baishan.nearshop.wxapi.WeiXinPayUtils;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class ConfirmOrdersPresenter extends BasePresenter<IConfirmOrdersView> {
    public ConfirmOrdersPresenter(IConfirmOrdersView mvpView) {
        super(mvpView);
    }

    public void getPreOrderInfo(OrdersParam param) {
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "GetFastPreOrderInfo");
        params.put("UserId", param.UserId);
        params.put("AreaProductId", param.AreaProductId);
        params.put("AddressId", param.AddressId);
        params.put("Num", param.Num);
        params.put("Remark", param.Remark);
        params.put("Spec", param.Spec);
        addSubscription(true,AppClient.getApiService()
                .getPreOrderInfo(params), new SubscriberCallBack<List<ConfirmOrderItem>>() {
            @Override
            protected void onSuccess(List<ConfirmOrderItem> response) {
                mvpView.getPreOrderInfoSuccess(response);
            }
        });
    }

    public void getPreOrderInfo(String shopcars, int userId) {
        addSubscription(true,AppClient.getApiService()
                .getPreOrderInfo("GetPreOrderInfo", shopcars, userId), new SubscriberCallBack<List<ConfirmOrderItem>>() {
            @Override
            protected void onSuccess(List<ConfirmOrderItem> response) {
                mvpView.getPreOrderInfoSuccess(response);
            }
        });
    }

    public void getLastBalance(String loginToken) {
        addSubscription(AppClient.getApiService().getLastBalance(loginToken), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.getLastBalanceSuccess(response);
            }

        });
    }

    public void generateFastOrder(int userId, List<ConfirmOrderItem> datas) {
        commitOrder("GenerateFastOrder", userId, datas);
    }

    public void generateOrder(int userId, List<ConfirmOrderItem> datas) {
        commitOrder("GenerateOrder", userId, datas);
    }

    public void commitOrder(String method, int userId, List<ConfirmOrderItem> datas) {
        String payType = datas.get(0).PayType;
        if ("支付宝".equals(payType)) {
            if (!AlipayUtils.isExist(context)) {
                ToastUtils.showToast(context.getString(R.string.tips_alipay_no));
                return;
            }
        } else {
            if (!WeiXinPayUtils.isExist(context)) {
                ToastUtils.showToast(context.getString(R.string.tips_alipay_no));
                return;
            }
        }
        addSubscription(true,AppClient.getApiService().commitOrder(method, userId + "", new Gson().toJson(datas)), new SubscriberCallBack<PayReq>() {
            @Override
            protected void onSuccess(PayReq response) {
                if (TextUtils.isEmpty(response.appId) && TextUtils.isEmpty(response.extData)) {
                    //余额支付
                } else {
                    if ("支付宝".equals(payType)) {
                        AlipayUtils.pay(context, response.extData);
                    } else {
                        WeiXinPayUtils.pay(context, response);
                    }
                }
                mvpView.commitOrderSuccess(response);
            }

        });
    }

    public void getGoodsOrderInfo(String orderNo) {
        addSubscription(true,AppClient.getApiService().getGoodsOrderInfo(orderNo), new SubscriberCallBack<List<ConfirmOrderItem>>() {
            @Override
            protected void onSuccess(List<ConfirmOrderItem> response) {
                if (response.size() > 0) {
                    mvpView.getPreOrderInfoSuccess(response);
                }
            }
        });
    }
}
