package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.view.ICoinsStoreView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class CoinsStorePresenter extends BasePresenter<ICoinsStoreView>{
    public CoinsStorePresenter(ICoinsStoreView mvpView) {
        super(mvpView);
    }

    public void getShopCoinsList(int areaId, int pageNow) {
        addSubscription(AppClient.getApiService().getShopCoinsList(areaId, pageNow), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                mvpView.getShopCoinsListSuccess(response);
            }
        });
    }
}
