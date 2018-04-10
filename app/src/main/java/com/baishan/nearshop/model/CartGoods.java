package com.baishan.nearshop.model;

/**
 * Created by RayYeung on 2016/11/18.
 */

public class CartGoods extends Goods {


    /**
     * CartId : 12
     * CartToken : 932917ca-1947-4567-bd39-bd3f7fee5f82
     * AreaId : 12
     * AreaProductId : 36
     * ProductId : 6
     * Title : null
     * Price : 600
     * ImageUrl : http://111.47.198.193:8033/Upload/Photos/20161115/cf4da1bd45f107b54cba07ad23dbf27a.png
     * Num : 1
     */

    public int CartId;
    public String CartToken;
    public boolean isChecked;

    public double totalPrice() {
        return Price * Num;
    }
}
