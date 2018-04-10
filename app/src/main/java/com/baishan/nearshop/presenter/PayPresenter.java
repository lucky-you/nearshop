package com.baishan.nearshop.presenter;

import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.OrdersPayInfo;
import com.baishan.nearshop.utils.AlipayUtils;
import com.baishan.nearshop.view.IPayView;
import com.baishan.nearshop.wxapi.WeiXinPayUtils;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RayYeung on 2016/11/24.
 */

public class PayPresenter extends BasePresenter<IPayView> {


    public PayPresenter(IPayView mvpView) {
        super(mvpView);
    }

    public void getPayInfo(OrdersPayInfo payInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "PayShopOrder");
        params.put("UserId", payInfo.UserId);
        params.put("OrderNo", payInfo.OrderNo);
        params.put("PayType", payInfo.PayType);
        params.put("Contact", payInfo.address.Contact);
        params.put("Phone", payInfo.address.Phone);
        params.put("Address", payInfo.address.Address);
        if (!TextUtils.isEmpty(payInfo.Remarks))
            params.put("Remarks", payInfo.Remarks);
        params.put("CouponId", payInfo.CouponId);
        getPayInfo(params);
    }

    public void getPayInfo(String orderNo, int userId, String payType, String price) {
        if (TextUtils.isEmpty(price)) {
            ToastUtils.showToast("请输入金额");
            return;
        }
        if ("0".equals(price)) {
            ToastUtils.showToast("金额必须大于0");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "PayOrder");
        params.put("OrderNo", orderNo);
        params.put("UserId", userId);
        params.put("PayType", payType);
        params.put("Price", price);
        getPayInfo(params);
    }

    public void recharge(int userId, String money, String payType) {
        if (TextUtils.isEmpty(money)) {
            ToastUtils.showToast("请输入金额");
            return;
        }
        if ("0".equals(money) || money.startsWith("0")) {
            ToastUtils.showToast("金额必须大于0");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "UserRecharge");
        params.put("UserId", userId);
        params.put("PayType", payType);
        params.put("OrderPrice", money);
        getPayInfo(params);
    }

    public void getPayInfo(Map<String, Object> params) {
        String payType = (String) params.get("PayType");
        if ("支付宝".equals(payType)) {
            if (!AlipayUtils.isExist(context)) {
                ToastUtils.showToast(context.getString(R.string.tips_alipay_no));
                return;
            }
        } else if ("微信".equals(payType)) {
            if (!WeiXinPayUtils.isExist(context)) {
                ToastUtils.showToast(context.getString(R.string.tips_alipay_no));
                return;
            }
        }
        addSubscription(true, AppClient.getApiService().getPayInfo(params), new SubscriberCallBack<PayReq>() {
                    @Override
                    protected void onSuccess(PayReq response) {
                        if (response != null) {
                            if (TextUtils.isEmpty(response.appId) && TextUtils.isEmpty(response.extData)) {
                                mvpView.balancePaySuccess();
                            } else {
                                if ("支付宝".equals(payType)) {
                                    AlipayUtils.pay(context, response.extData);
                                } else {
                                    WeiXinPayUtils.pay(context, response);
                                }
                                mvpView.getPayInfoSuccess();
                            }
                        } else {
                            ToastUtils.showToast("获取数据失败");
                        }
                    }

                }

        );
    }

}
