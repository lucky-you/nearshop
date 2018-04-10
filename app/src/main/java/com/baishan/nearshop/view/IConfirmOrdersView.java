package com.baishan.nearshop.view;

import com.baishan.nearshop.model.ConfirmOrderItem;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public interface IConfirmOrdersView {
    void getPreOrderInfoSuccess(List<ConfirmOrderItem> response);

    void getLastBalanceSuccess(String response);

    void commitOrderSuccess(PayReq response);


}
