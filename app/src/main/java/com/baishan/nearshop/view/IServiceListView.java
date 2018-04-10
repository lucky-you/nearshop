package com.baishan.nearshop.view;

import com.baishan.nearshop.model.EasyService;

import java.util.List;

/**
 * Created by RayYeung on 2016/10/14.
 */

public interface IServiceListView {
    void getServiceListSuccess(List<EasyService> response);

    void reservationServiceSuccess(String response);
}
