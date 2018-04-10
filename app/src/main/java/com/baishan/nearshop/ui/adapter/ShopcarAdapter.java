package com.baishan.nearshop.ui.adapter;

import android.text.TextUtils;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.CartGoods;
import com.baishan.nearshop.model.Shopcar;
import com.baishan.nearshop.model.ShopcarItem;
import com.baishan.nearshop.utils.StrUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/20.
 */
public class ShopcarAdapter extends BaseMultiItemQuickAdapter<ShopcarItem> {

    public ShopcarAdapter(List<ShopcarItem> data) {
        super(data);
        addItemType(ShopcarItem.ADDRESS, R.layout.item_order_address);
        addItemType(ShopcarItem.GOODS, R.layout.item_shopcar_goods);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ShopcarItem item) {
        switch (baseViewHolder.getItemViewType()) {
            case ShopcarItem.ADDRESS:
                Shopcar address = item.address;
                baseViewHolder.setText(R.id.tvArea, address.concatPartAddress())
                        .setText(R.id.tvAddress, address.AddressInfo.get(0).Address+" "+address.AddressInfo.get(0).concatInfo())
                        //.setText(R.id.tvUserInfo, address.AddressInfo.get(0).concatInfo())
                        .setChecked(R.id.cbArea,item.isChecked)
                        .setOnClickListener(R.id.cbArea,new OnItemChildClickListener())
                        .setOnClickListener(R.id.tvUpdate,new OnItemChildClickListener())
                        .setOnClickListener(R.id.tvSelectOther,new OnItemChildClickListener());
                break;
            case ShopcarItem.GOODS:
                CartGoods goods = item.goods;
                ImageLoaderUtils.displayGoods(goods.getImage(), baseViewHolder.getView(R.id.ivPic));
                baseViewHolder.setText(R.id.tvTitle, goods.Title)
                        .setText(R.id.tvPrice, StrUtils.formatPrice(mContext,goods.Price))
                        .setText(R.id.tvNum, goods.Num + "")
                        .setChecked(R.id.cbGoods,item.goods.isChecked)
                        .setOnClickListener(R.id.cbGoods,new OnItemChildClickListener())
                        .setOnClickListener(R.id.tvNum,new OnItemChildClickListener())
                        .setOnClickListener(R.id.ivDelete,new OnItemChildClickListener())
                        .setOnClickListener(R.id.btnMinus,new OnItemChildClickListener())
                        .setOnClickListener(R.id.btnAdd,new OnItemChildClickListener());
                if(!TextUtils.isEmpty(goods.Spec)){
                    baseViewHolder.setText(R.id.tvSpec,"规格："+ goods.Spec);
                }else{
                    baseViewHolder.setText(R.id.tvSpec,"");
                }
                break;
        }
    }
}
