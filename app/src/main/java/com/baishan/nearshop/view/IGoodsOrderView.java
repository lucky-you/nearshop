package com.baishan.nearshop.view;

import com.baishan.nearshop.model.ShopOrder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public interface IGoodsOrderView extends IBaseView{
    void getShopOrderListSuccess(List<ShopOrder> response);
}
