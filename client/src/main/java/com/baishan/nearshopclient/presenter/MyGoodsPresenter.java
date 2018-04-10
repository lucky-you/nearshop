package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.Goods;
import com.baishan.nearshopclient.view.IMyGoodsView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class MyGoodsPresenter extends BasePresenter<IMyGoodsView> {

    public MyGoodsPresenter(IMyGoodsView mvpView) {
        super(mvpView);
    }

    public void getStoreProducts(int id, int pageNow) {
        addSubscription(AppClient.getApiService().getStoreProducts(id + "", pageNow), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                mvpView.getStoreProductsSuccess(response);
            }
        });
    }
}
