package com.baishan.nearshop.ui.adapter;

import android.widget.ImageView;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.StrUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/20.
 */
public class GoodsAdapter extends BaseQuickAdapter<Goods> {
    /**
     * S币兑换商城
     */
    public static int GOODS_TYPE_COINS = 1;
    private int mType;

    public GoodsAdapter(List<Goods> data) {
        super(R.layout.item_list_goods, data);
    }

    public GoodsAdapter(List<Goods> data, int type) {
        super(R.layout.item_list_goods, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Goods goods) {
        ImageView ivPic = baseViewHolder.getView(R.id.ivPic);
        ImageLoaderUtils.displayGoods(goods.getImage(), ivPic);
        baseViewHolder.setText(R.id.tvTitle, goods.Title)
                .setOnClickListener(R.id.btnAdd, new OnItemChildClickListener());
        if (mType == GOODS_TYPE_COINS) {
            //S币兑换
            baseViewHolder.setText(R.id.btnAdd, "立即兑换")
                    .setText(R.id.tvMoney, StrUtils.formatCoin(mContext, goods.Price))
                    .setTextColor(R.id.tvMoney, mContext.getResources().getColor(R.color.font_coins_blue));
        } else {
            baseViewHolder.setText(R.id.btnAdd, goods.LinkLevel == ConstantValue.CATEGORYID ? "加购物车" : "立即购买")
                    .setText(R.id.tvMoney, StrUtils.formatPrice(mContext, goods.Price));
        }
    }
}
