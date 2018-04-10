package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.SearchHistory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class SearchAdapter extends BaseQuickAdapter<SearchHistory> {
    public SearchAdapter(List<SearchHistory> data) {
        super(R.layout.item_area, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SearchHistory item) {
        baseViewHolder.setText(R.id.tvArea, item.getKey());
    }
}
