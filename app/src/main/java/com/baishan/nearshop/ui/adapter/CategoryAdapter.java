package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Category;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/10/11.
 */

public class CategoryAdapter extends BaseQuickAdapter<Category> {

    public CategoryAdapter(List<Category> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Category category) {
        baseViewHolder.setText(R.id.tvName,category.Title);

    }
}
