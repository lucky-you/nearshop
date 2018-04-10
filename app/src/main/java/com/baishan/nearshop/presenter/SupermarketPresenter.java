package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Category;
import com.baishan.nearshop.view.ISupermarketView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class SupermarketPresenter extends BasePresenter<ISupermarketView> {
    public SupermarketPresenter(ISupermarketView mvpView) {
        super(mvpView);
    }


    public void getAllCategory(Map<String,Object> params){
        addSubscription(AppClient.getApiService().getAllCategory(params), new SubscriberCallBack<List<Category>>() {
            @Override
            protected void onSuccess(List<Category> response) {
                mvpView.getAllCategorySuccess(response);
            }
        });
    }
}
