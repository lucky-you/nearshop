package com.baishan.nearshop.view;

import com.baishan.nearshop.model.Area;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public interface IAddrManageView {
    void deleteSuccess();

    void setDefaultSuccess();

    void getAddressListSuccess(List<Area> response);
}
