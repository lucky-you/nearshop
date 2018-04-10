package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.view.IDefaultAddressView;

/**
 * Created by RayYeung on 2016/11/17.
 */

public class DefaultAddressPresenter extends BasePresenter<IDefaultAddressView> {

    public DefaultAddressPresenter(IDefaultAddressView mvpView) {
        super(mvpView);
    }


    public void getDefaultAddress(int areaId, int userId){
        addSubscription(AppClient.getApiService().getUserDefaultAddress(areaId, userId), new SubscriberCallBack<Area>() {
            @Override
            protected void onSuccess(Area response) {
                if(response!=null){
                    mvpView.getDefaultAddressSuccess(response);
                }
            }
        });
    }
}
