package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.IAddAddrView;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class AddAddrPresenter extends BasePresenter<IAddAddrView>{
    public AddAddrPresenter(IAddAddrView mvpView) {
        super(mvpView);
    }


    public void doAddress(Map<String,Object> params){
        addSubscription(AppClient.getApiService().address(params), new SubscriberCallBack<Boolean>() {
            @Override
            protected void onSuccess(Boolean response) {
                if ("AddAddress".equals(params.get("Method"))) {
                    mvpView.addSuccess();
                } else {
                    mvpView.editSuccess();
                }
            }
        });
    }
}
