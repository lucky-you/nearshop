package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.Goods;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface IMyGoodsView {
    void getStoreProductsSuccess(List<Goods> response);
}
