package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.view.IGoodsListView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class GoodsListPresenter extends BasePresenter<IGoodsListView> {
    public GoodsListPresenter(IGoodsListView mvpView) {
        super(mvpView);
    }

    public void getShopCustomList(int categoryId, int pageNow) {
        addSubscription(AppClient.getApiService().getShopCustomList(categoryId, pageNow), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                mvpView.getShopCustomListSuccess(response);
            }
        });
    }
}
