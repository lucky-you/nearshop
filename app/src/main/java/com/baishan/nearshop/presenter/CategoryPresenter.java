package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.view.ICategoryView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class CategoryPresenter extends BasePresenter<ICategoryView> {
    public CategoryPresenter(ICategoryView mvpView) {
        super(mvpView);
    }


    public void getGoodsList(Map<String,Object> params) {
        addSubscription(AppClient.getApiService().getGoodsList(params), new SubscriberCallBack<List<Goods>>() {
            @Override
            protected void onSuccess(List<Goods> response) {
                mvpView.getGoodsListSuccess(response);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mvpView.stopRefresh();
            }
        });
    }
}
