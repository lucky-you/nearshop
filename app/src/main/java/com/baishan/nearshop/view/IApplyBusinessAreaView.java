package com.baishan.nearshop.view;

import android.net.Uri;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public interface IApplyBusinessAreaView {
    void onSelectAlbumFinish(Uri data);

    void onCameraCancel();

    void onCameraFinish();

    void onApplyPartnerSuccess();
}
