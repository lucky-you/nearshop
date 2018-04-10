package com.baishan.nearshop.view;

import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.model.GridType;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface IEasyOrderView extends IBaseView{
    void getTypeListSuccess(List<GridType> response);

    void getRecommendListSuccess(List<EasyService> response);

    void reservationServiceSuccess(String response);


}
