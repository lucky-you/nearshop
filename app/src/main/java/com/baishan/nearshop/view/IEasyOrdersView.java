package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Orders;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public interface IEasyOrdersView extends IBaseView {
    void getServiceOrderListSuccess(List<Orders> response);

}
