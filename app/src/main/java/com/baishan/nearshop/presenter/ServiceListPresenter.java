package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.view.IServiceListView;

import java.util.List;
import java.util.Map;

/**
 * Created by RayYeung on 2016/10/14.
 */

public class ServiceListPresenter extends BasePresenter<IServiceListView> {

    public ServiceListPresenter(IServiceListView mvpView) {
        super(mvpView);
    }



    public void getServiceList(Map<String, Object> params){
        addSubscription(AppClient.getApiService().getServiceList(params), new SubscriberCallBack<List<EasyService>>() {
            @Override
            protected void onSuccess(List<EasyService> response) {
                mvpView.getServiceListSuccess(response);
            }
        });
    }
    public void reservationService(Area area, Address address, String remark, int userId, int serviceId) {
        addSubscription(AppClient.getApiService()
                .reservationService("ReservationService", userId + "", area.AreaId + "", serviceId + "", address.Contact,address.Phone,address.Address,remark), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.reservationServiceSuccess(response);
            }
        });
    }
}
