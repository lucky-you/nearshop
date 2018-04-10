package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.view.IAddrManageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class AddrManagePresenter extends BasePresenter<IAddrManageView> {
    public AddrManagePresenter(IAddrManageView mvpView) {
        super(mvpView);
    }


    public  void getAddressList(int userId,int areaId){
        addSubscription(AppClient.getApiService().getAddressList(userId,areaId), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                mvpView.getAddressListSuccess(response);
            }
        });
    }

    public void doAddress(Map<String,Object> params) {
        addSubscription(AppClient.getApiService().address(params), new SubscriberCallBack<Boolean>() {
            @Override
            protected void onSuccess(Boolean response) {
                if ("DeleteAddress".equals(params.get("Method"))) {
                    mvpView.deleteSuccess();
                } else {
                    mvpView.setDefaultSuccess();
                }
            }
        });
    }
}
