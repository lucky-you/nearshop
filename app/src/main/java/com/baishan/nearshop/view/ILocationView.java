package com.baishan.nearshop.view;

import com.amap.api.location.AMapLocation;
import com.baishan.nearshop.model.Area;

import java.util.List;

/**
 * Created by RayYeung on 2016/10/17.
 */

public interface ILocationView {
    void locationSuccess(AMapLocation aMapLocation);

    void locationFailure();

    void getCommonAreaSuccess(List<Area> response);
}
