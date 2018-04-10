package com.baishan.nearshopclient.ui.adapter;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.Goods;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class GoodsAdapter extends BaseQuickAdapter<Goods> {
    public GoodsAdapter(List<Goods> data) {
        super(R.layout.item_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Goods goods) {
        baseViewHolder.setText(R.id.tvTitle, goods.Title)
                .setText(R.id.tvPrice, "￥" + goods.Price);
        ImageLoaderUtils.displayGoods(goods.getImage(), baseViewHolder.getView(R.id.ivPic));
    }
}
