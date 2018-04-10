package com.baishan.nearshop.view;

import android.net.Uri;

import com.baishan.nearshop.model.Area;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public interface IApplyPartnerView {
    void onSelectAlbumFinish(Uri data);


    void onCameraCancel();

    void onCameraFinish();

    void getShopAreaListSuccess(List<Area> response);

    void onApplyPartnerSuccess();
}
