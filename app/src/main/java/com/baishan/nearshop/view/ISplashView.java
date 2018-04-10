package com.baishan.nearshop.view;

import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.model.Area;

/**
 * Created by RayYeung on 2016/10/18.
 */

public interface ISplashView {

    void onServerError();

    void onLoginFailure(ResultResponse response);

    void getUserCurrentAreaSuccess(Area response);

    void getUserCurrentAreaFailure();

    void getAreaInfoSuccess(Area area);

    void getAreaInfoFail(int areaId);
}
