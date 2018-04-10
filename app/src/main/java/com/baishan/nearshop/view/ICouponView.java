package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Coupon;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public interface ICouponView {
    void onGetCouponsSuccess(List<Coupon> response);
}
