package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Goods;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface ICategoryView  extends IBaseView{

    void getGoodsListSuccess(List<Goods> response);

}
