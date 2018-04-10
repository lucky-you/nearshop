package com.baishan.mylibrary.utils;

import android.graphics.Bitmap;

import com.baishan.mylibrary.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * ImageLoader加载图片的配置类
 */
public class ImageOptHelper {

    public static DisplayImageOptions.Builder getBaseBuilder() {
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565);
    }


    public static DisplayImageOptions getImgOptions() {
        return getBaseBuilder()
                .showImageOnLoading(R.drawable.image_loading)
                .showImageForEmptyUri(R.drawable.image_loading)
                .showImageOnFail(R.drawable.image_loading)
                .build();
    }

    public static DisplayImageOptions getGoodsOptions() {
        return getBaseBuilder()
                .showImageOnLoading(R.drawable.pic_loading)
                .showImageForEmptyUri(R.drawable.pic_loading)
                .showImageOnFail(R.drawable.pic_loading)
                .build();
    }


    public static DisplayImageOptions getBigImgOptions() {
        return getBaseBuilder().build();
    }

    public static DisplayImageOptions getAvatarOptions() {
        return getBaseBuilder()
                .showImageOnLoading(R.drawable.image_loading)
                .showImageForEmptyUri(R.drawable.image_loading)
                .showImageOnFail(R.drawable.image_loading)
                .build();
    }

    public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.image_loading)
                .showImageForEmptyUri(R.drawable.image_loading)
                .showImageOnFail(R.drawable.image_loading)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
        return options;
    }
}
