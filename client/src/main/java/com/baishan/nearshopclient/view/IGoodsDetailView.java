package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.Goods;
import com.baishan.nearshopclient.model.ShopStore;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public interface IGoodsDetailView {
    void getShopStoreListSuccess(List<ShopStore> response);

    void getGoodsInfoSuccess(Goods goods);
}
