package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.view.IServiceDetalView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class ServiceDetailPresenter extends BasePresenter<IServiceDetalView> {
    public ServiceDetailPresenter(IServiceDetalView mvpView) {
        super(mvpView);
    }

    public void reservationService(Area area, Address address, String remark, int userId, int serviceId) {
        addSubscription(AppClient.getApiService()
                .reservationService("ReservationService", userId + "", area.AreaId + "", serviceId + "", address.Contact, address.Phone, address.Address, remark), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.reservationServiceSuccess(response);
            }
        });
    }


    public void getServiceDetail(int id) {
        addSubscription(AppClient.getApiService().getServiceInfo(id), new SubscriberCallBack<List<EasyService>>() {
            @Override
            protected void onSuccess(List<EasyService> response) {
                if (response.size() > 0) {
                    mvpView.getServiceDetailSuccess(response.get(0));
                }
            }
        });
    }
}
