package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Banner;
import com.baishan.nearshop.model.FirstProduct;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.model.HotNews;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface IHomeView extends IBaseView {
    void getBannerSuccess(List<Banner> response);

    void getFastServiceSuccess(List<GridType> response);

    void onHotNewsSuccess(List<HotNews> response);

    void onFirstProductSuccess(List<FirstProduct> response);

    void getHotShopListSuccess(List<Goods> response);

}
