package com.baishan.nearshop.view;

import android.net.Uri;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public interface IPersonInfoView {
    void onSelectAlbumFinish(Uri data);

    void onCameraFinish();

    void onCropSuccess();

    void onCameraCancel();

    void onUploadSuccess(String imageUrl);

    void onEditSexSuccess( String value);

    void onEditNickNameSuccess(String value);

    void onEditAgeSuccess(String value);
}
