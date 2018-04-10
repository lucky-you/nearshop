package com.baishan.nearshopclient.ui.adapter;

import android.content.Intent;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.ShopOrder;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.activity.GoodsOrdersDetailActivity;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.utils.OrdersConstants;
import com.baishan.nearshopclient.utils.StrUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ShopOrdersAdapter extends BaseMultiItemQuickAdapter<ShopOrder> {
    private int type;
    private int index;

    public ShopOrdersAdapter(List<ShopOrder> data, int type, int index) {
        super(data);
        addItemType(ShopOrder.AREA, R.layout.item_orders_business);
        addItemType(ShopOrder.GOODS, R.layout.item_orders_goods);
        addItemType(ShopOrder.INFO, R.layout.item_orders_info);
        this.type = type;
        this.index = index;
    }

    @Override
    protected void convert(BaseViewHolder holder, ShopOrder order) {
        switch (holder.getItemViewType()) {
            case ShopOrder.AREA:
                if (type == UserInfo.ADMIN) {
                    holder.setText(R.id.tvArea, order.AreaName)
                            .setText(R.id.tvState, order.StateName);
                } else {//商家
                    holder.setText(R.id.tvArea, String.format(mContext.getString(R.string.orders_sender_phone), order.CourierName, order.CourierPhone))
                            .setText(R.id.tvState, order.StateName)
                            .setVisible(R.id.ivArrow, false);
                }
                break;
            case ShopOrder.GOODS:
                ImageLoaderUtils.displayGoods(order.GoodItem.getImage(), holder.getView(R.id.goods_img));
                holder.setText(R.id.goods_name, order.GoodItem.Title)
                        .setText(R.id.goods_price, StrUtils.formatPrice(mContext, order.GoodItem.Price))
                        .setText(R.id.goods_number, "X" + order.GoodItem.Num);
                break;
            case ShopOrder.INFO:
                holder.setText(R.id.tvOrderInfo, String.format(mContext.getString(R.string.goods_orders_info), order.Num, order.OrderPrice + order.SubBalance, order.CourierPrice))
                        .setText(R.id.tv_orderTime, "下单时间：" + order.CreateTime);

                if (type == UserInfo.ADMIN) {
                    if (order.OrderState == OrdersConstants.ORDER_STATE_MATCH_SENDER) {
                        holder.setVisible(R.id.llOperate, true)
                                .setVisible(R.id.btnRight, true)
                                .setText(R.id.btnRight, "重新派送")
                                .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                    } else {
                        holder.setVisible(R.id.llOperate, false);
                    }
                } else if (index == 0 || index == 2) {// 0 商户待处理   2 确认退款
                    holder.setVisible(R.id.llOperate, true)
                            .setVisible(R.id.btnRight, true)
                            .setText(R.id.btnRight, "确认")
                            .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                }
                break;
        }
        holder.getConvertView().setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsOrdersDetailActivity.class);
            intent.putExtra(ConstantValue.ORDER_NO, order.OrderNo);
            if (type == UserInfo.BUSINESS && index == 0) {
                intent.putExtra(ConstantValue.TYPE, "DCL");
            }
            mContext.startActivity(intent);
        });
    }
}
