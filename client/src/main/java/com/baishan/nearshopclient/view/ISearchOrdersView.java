package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.SenderGoodsOrders;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public interface ISearchOrdersView {
    void searchKeywordsSuccess(List<SenderGoodsOrders> response);

    void changeStateSuccess(String response);
}
