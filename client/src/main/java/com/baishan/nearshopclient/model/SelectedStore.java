package com.baishan.nearshopclient.model;

/**
 * Created by Administrator on 2016/12/5 0005.
 */

public class SelectedStore {
    public String CartToken;
    public int StoreId;

    public SelectedStore(String cartToken, int storeId) {
        CartToken = cartToken;
        StoreId = storeId;
    }
}
