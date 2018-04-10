package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.view.IRechargeView;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class RechargePresenter extends BasePresenter<IRechargeView> {
    public RechargePresenter(IRechargeView mvpView) {
        super(mvpView);
    }

    public void commitRecharge(int userId, int money, String payWay) {

    }
}
