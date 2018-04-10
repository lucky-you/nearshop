package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Coupon;
import com.baishan.nearshop.view.ICouponView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class CouponPresenter extends BasePresenter<ICouponView> {
    public CouponPresenter(ICouponView mvpView) {
        super(mvpView);
    }

    public void getUserCoupons(String method,int areaId, int userId, int price) {
        addSubscription(AppClient.getApiService().getUserCoupons(method,areaId, userId, price), new SubscriberCallBack<List<Coupon>>() {
            @Override
            protected void onSuccess(List<Coupon> response) {
                mvpView.onGetCouponsSuccess(response);
            }

        });
    }
}
