package com.baishan.nearshop.ui.adapter;

import android.content.Intent;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Orders;
import com.baishan.nearshop.ui.activity.ServiceOrderDetailActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22 0022.
 * 便民订单adapter
 */

public class EasyOrdersAdapter extends BaseQuickAdapter<Orders> {

    public EasyOrdersAdapter(List<Orders> data) {
        super(R.layout.item_easy_orders, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Orders order) {
        holder.setVisible(R.id.llOperate, false)
                .setVisible(R.id.tvAuto, false);
        switch (order.OrderState) {
            case ConstantValue.ORDER_STATE_MATCH_SENDER: //0分配派送员
                holder.setVisible(R.id.llOperate, true)
                        .setVisible(R.id.btnLeft, false)
                        .setText(R.id.btnRight, mContext.getString(R.string.orders_state_cancel))
                        .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                break;
            case ConstantValue.ORDER_STATE_RESERVED://10已预约
                holder.setVisible(R.id.llOperate, true)
                        .setVisible(R.id.btnLeft, false)
                        .setText(R.id.btnRight, mContext.getString(R.string.orders_state_cancel))
                        .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                break;
            case ConstantValue.ORDER_STATE_RECEIVIED_ORDER://20已接单
                holder.setVisible(R.id.llOperate, true)
                        .setVisible(R.id.btnLeft, false)
                        .setText(R.id.btnRight, mContext.getString(R.string.orders_state_cancel))
                        .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                break;
            case ConstantValue.ORDER_STATE_START_WORK: //25 开始工作
                holder.setVisible(R.id.llOperate, false);
                break;
            case ConstantValue.ORDER_STATE_FINISH_WORK://30完成工作
                holder.setVisible(R.id.llOperate, true)
                        .setVisible(R.id.btnLeft, false)
                        .setText(R.id.btnRight, mContext.getString(R.string.orders_state_pay))
                        .setOnClickListener(R.id.btnRight, v -> gotoDetail(order));
                break;
            case ConstantValue.ORDER_STATE_PAID: //35已付款
                holder.setVisible(R.id.llOperate, true)
                        .setVisible(R.id.btnLeft, true)
                        .setText(R.id.btnLeft, mContext.getString(R.string.orders_state_confirm))
                        .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener())
                        .setVisible(R.id.tvAuto, true)
                        .setText(R.id.tvAuto, mContext.getString(R.string.Prompt_03));
                if (order.IsRefund == 1) {
                    holder.setText(R.id.btnRight, mContext.getString(R.string.orders_state_refund))
                            .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                }
                break;
            case ConstantValue.ORDER_STATE_FINISHED://40已完成
                if (order.IsRefund == 1) {
                    holder.setVisible(R.id.llOperate, true)
                            .setVisible(R.id.btnLeft, false)
                            .setText(R.id.btnRight, mContext.getString(R.string.orders_state_refund))
                            .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                }
                break;
            case ConstantValue.ORDER_STATE_SENDER_CANCEL_ORDER://50派送员取消订单
                holder.setVisible(R.id.llOperate, false);
                break;
            case ConstantValue.ORDER_STATE_USER_CANCEL_ORDER://55用户取消订单
                holder.setVisible(R.id.llOperate, false);
                break;
            case ConstantValue.ORDER_STATE_APPLY_REFUND://60申请退款
                holder.setVisible(R.id.llOperate, true)
                        .setVisible(R.id.btnRight, true)
                        .setText(R.id.btnRight, mContext.getString(R.string.orders_state_confirm))
                        .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                break;
            case ConstantValue.ORDER_STATE_REJECT_REFUND://65拒绝退款
                if (order.IsRefund == 1) {
                    holder.setVisible(R.id.llOperate, true)
                            .setVisible(R.id.btnLeft, true)
                            .setText(R.id.btnLeft, mContext.getString(R.string.orders_state_confirm))
                            .setVisible(R.id.btnRight, true)
                            .setText(R.id.btnRight, mContext.getString(R.string.orders_state_refund))
                            .setVisible(R.id.tvAuto, true)
                            .setVisible(R.id.tvPriceDesc, false)//不顯示訂單信息
                            .setText(R.id.tvAuto, mContext.getString(R.string.Prompt_04))
                            .setOnClickListener(R.id.btnRight, new OnItemChildClickListener())
                            .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener());
                }
                break;
            case ConstantValue.ORDER_STATE_REFUND_FINISHED://70退款成功
                holder.setVisible(R.id.llOperate, false);
                break;
        }
        ImageLoaderUtils.displayGoods(order.getImage(), holder.getView(R.id.goods_img));
        holder.setText(R.id.tvArea, order.AreaName)
                .setText(R.id.goods_name, order.Title)
                .setText(R.id.tvState, order.StateName)
                .setText(R.id.goods_attr, order.Name)
                .setText(R.id.goods_price, order.Price)
                .setText(R.id.tvPriceDesc, order.StateMessage);
    }

    private void gotoDetail(Orders order) {
        Intent it = new Intent(mContext, ServiceOrderDetailActivity.class);
        it.putExtra(ConstantValue.DATA, order);
        mContext.startActivity(it);
    }
}
