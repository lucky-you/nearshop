package com.baishan.nearshop.ui.adapter;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.GridType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class GridTypeAdapter extends BaseQuickAdapter<GridType> {

    public GridTypeAdapter(List<GridType> data) {
        super(R.layout.item_grid_type, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GridType gridType) {
        ImageLoaderUtils.displayImage(gridType.ImageUrl, baseViewHolder.getView(R.id.ivImg));
        baseViewHolder.setText(R.id.tvTitle,gridType.Title)
        .setOnClickListener(R.id.llItem,new OnItemChildClickListener());

    }
}
