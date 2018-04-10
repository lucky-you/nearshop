package com.baishan.nearshop.view;

import com.baishan.nearshop.model.EasyService;

/**
 * Created by RayYeung on 2016/9/21.
 */
public interface IServiceDetalView {
    void reservationServiceSuccess(String response);

    void getServiceDetailSuccess(EasyService easyService);
}
