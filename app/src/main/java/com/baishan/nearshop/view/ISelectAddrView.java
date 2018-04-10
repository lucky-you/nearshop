package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.City;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/22.
 */
public interface ISelectAddrView {
    void getAreaListSuccess(List<Area> response);

    void getShopAreaListSuccess(List<Area> response);


    void getHotCitiesSuccess(List<City> response);

    void searchAreaSuccess(List<Area> response);
}
