package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.ShopStore;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public interface IGoodsDetailView {
    void getShopStoreListSuccess(List<ShopStore> response);

    void getGoodsInfoSuccess(Goods goods);

    void getAreaInfoSuccess(Area area);

    void getAreaInfoFail(int areaId);
}
