package com.baishan.nearshop.view;

import com.baishan.nearshop.model.ShopOrder;

import java.util.List;

/**
 * Created by RayYeung on 2016/12/7.
 */

public interface IExchangeRecordView {
    void getRecordSuccess(List<ShopOrder> response);
}
