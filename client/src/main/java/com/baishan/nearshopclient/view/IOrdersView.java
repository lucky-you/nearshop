package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.model.ServiceOrders;
import com.baishan.nearshopclient.model.ShopOrder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface IOrdersView {
    void getServiceOrderListSuccess(List<ServiceOrders> response);

    void changeStateSuccess(String response, String orderNo);

    void getSenderShopOrderListSuccess(List<SenderGoodsOrders> response);

    void getShopOrderListSuccess(List<ShopOrder> response);

    void reDistributeSuccess();

    void getAdminServiceOrderListSuccess(List<Orders> response);

    void confirmSupplySuccess();

}
