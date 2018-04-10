package com.baishan.nearshop.view;

import java.util.ArrayList;

/**
 * Created by RayYeung on 2017/1/9.
 */

public interface IPostingView {
    void showLoading();

    void hideLoading();

    void onCameraCancel();

    void onCameraFinish();

    void onImageFinish(ArrayList<String> list);
}
