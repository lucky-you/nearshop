package com.baishan.nearshop.ui.adapter;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.ui.activity.PostingActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2017/1/9.
 */

public class PostImageAdapter extends BaseQuickAdapter<String> {

    public PostImageAdapter(List<String> data) {
        super(R.layout.item_grid_image, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        if (s.equals(PostingActivity.ADD_FLAG)) {
            holder.setImageResource(R.id.img, R.drawable.compose_pic_add)
                    .setOnClickListener(R.id.img,new OnItemChildClickListener())
                    .setVisible(R.id.ivDelete, false);
        } else {
            ImageLoaderUtils.displayImage("file://" + s, holder.getView(R.id.img));
            holder.setVisible(R.id.ivDelete, true)
                    .setOnClickListener(R.id.img,v -> {})
                    .setOnClickListener(R.id.ivDelete,v -> {
                        int position = getViewHolderPosition(holder);
                        getData().remove(position);
                        notifyItemRemoved(position);
                    });
        }
    }
}
