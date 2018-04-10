package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.SenderOrdersDetail;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface IGoodsOrderDetailView {

    void getGoodsOrderInfoSuccess(SenderOrdersDetail senderOrdersDetail);

    void changeStateSuccess(String response);

    void reDistributeSuccess();

    void confirmSupplySuccess();
}
