package com.baishan.nearshopclient.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshopclient.model.Banner;
import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class ImageHolderView implements Holder<Banner> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context, int position, final Banner data) {
        ImageLoaderUtils.displayImage(data.ImageUrl, imageView);
    }

}
