package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Shopcar;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface IShopCarView extends IBaseView {
    void getMyShopCartSuccess(List<Shopcar> response);

    void changeAddressSuccess();

    void deleteGoodsSuccess();

    void changeNumSuccess();
}
