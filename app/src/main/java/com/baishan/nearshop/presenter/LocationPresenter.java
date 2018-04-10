package com.baishan.nearshop.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.view.ILocationView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by RayYeung on 2016/10/17.
 */

public class LocationPresenter extends BasePresenter<ILocationView> implements AMapLocationListener {
    private AMapLocationClient mlocationClient;

    public LocationPresenter(ILocationView mvpView) {
        super(mvpView);
    }

    public void startLocation(Context context) {
        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    public void stopLocation() {
        mlocationClient.stopLocation();
    }

    @Override
    public void detachView() {
        super.detachView();
        mlocationClient.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                mvpView.locationSuccess(aMapLocation);
            } else if (aMapLocation.getErrorCode() == 12) {
                mvpView.locationFailure();
            } else {
                mvpView.locationFailure();
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        } else {
            mvpView.locationFailure();
        }
    }

    public void getCommonArea(int userId) {
        addSubscription(AppClient.getApiService().getAddressList(userId, -1), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                mvpView.getCommonAreaSuccess(response);
            }
        });
    }

}
