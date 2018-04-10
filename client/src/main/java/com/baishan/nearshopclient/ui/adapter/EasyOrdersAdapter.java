package com.baishan.nearshopclient.ui.adapter;

import android.content.Intent;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.ui.activity.ServiceOrderDetailActivity;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class EasyOrdersAdapter extends BaseQuickAdapter<Orders> {


    public EasyOrdersAdapter(List<Orders> data) {
        super(R.layout.item_easy_orders, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Orders order) {
        ImageLoaderUtils.displayImage(order.getImage(), holder.getView(R.id.goods_img));
        holder.setText(R.id.tvArea, order.AreaName)
                .setText(R.id.tvState, order.StateName)
                .setText(R.id.goods_name, order.Title)
                .setText(R.id.goods_attr, order.Name)
                .setText(R.id.goods_price, order.Price)
                .setText(R.id.tvPriceDesc, order.StateMessage);
        holder.getConvertView().setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ServiceOrderDetailActivity.class);
            intent.putExtra(ConstantValue.ORDER_NO, order.OrderNo);
            mContext.startActivity(intent);
        });
    }


}
