package com.baishan.nearshopclient.view;


import com.baishan.nearshopclient.model.Orders;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface IServiceOrderDetailView {
    void getOrderInfoSuccess(Orders orders);

    void reDistributeSuccess();
}
