package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.view.IEasyOrderView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class EasyOrderPresenter extends BasePresenter<IEasyOrderView>{
    public EasyOrderPresenter(IEasyOrderView mvpView) {
        super(mvpView);
    }

    public void getTypeList(Map<String, Object> params) {
        addSubscription(AppClient.getApiService().getServiceFast(params), new SubscriberCallBack<List<GridType>>() {
            @Override
            protected void onSuccess(List<GridType> response) {
                mvpView.getTypeListSuccess(response);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mvpView.stopRefresh();
            }
        });
    }

    public void getRecommendServiceList( int areaId,int pageNow) {
        addSubscription(AppClient.getApiService().getRecommendServiceList(areaId,pageNow), new SubscriberCallBack<List<EasyService>>() {
            @Override
            protected void onSuccess(List<EasyService> response) {
                mvpView.getRecommendListSuccess(response);
            }
        });
    }
    public void getSearchRecommendServiceList( Map<String, Object> params) {
        addSubscription(AppClient.getApiService().getSearchRecommendServiceList(params), new SubscriberCallBack<List<EasyService>>() {
            @Override
            protected void onSuccess(List<EasyService> response) {
                mvpView.getRecommendListSuccess(response);
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
