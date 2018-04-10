package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.ShopOrder;
import com.baishan.nearshop.view.IExchangeRecordView;

import java.util.List;

/**
 * Created by RayYeung on 2016/12/7.
 */

public class ExchangeRecordPresenter extends BasePresenter<IExchangeRecordView> {

    public ExchangeRecordPresenter(IExchangeRecordView mvpView) {
        super(mvpView);
    }

    public void getRecord(int userId) {
        addSubscription(AppClient.getApiService().getRecord(userId), new SubscriberCallBack<List<ShopOrder>>() {
            @Override
            protected void onSuccess(List<ShopOrder> response) {
                mvpView.getRecordSuccess(response);
            }
        });
    }
}
