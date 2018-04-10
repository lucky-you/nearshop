package com.baishan.nearshop.ui.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.ShopOrder;
import com.baishan.nearshop.ui.activity.ConfirmOrdersActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.OrdersConstants;
import com.baishan.nearshop.utils.PermissionHelper;
import com.baishan.nearshop.utils.StrUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 * 商品订单adapter
 */
public class ShopOrdersAdapter extends BaseMultiItemQuickAdapter<ShopOrder> {
    private int type;

    public ShopOrdersAdapter(List<ShopOrder> data) {
        this(data, 1);
    }

    public ShopOrdersAdapter(List<ShopOrder> data, int type) {
        super(data);
        addItemType(ShopOrder.AREA, R.layout.item_orders_business);
        addItemType(ShopOrder.GOODS, R.layout.item_orders_goods);
        addItemType(ShopOrder.INFO, R.layout.item_orders_info);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, ShopOrder order) {
        switch (holder.getItemViewType()) {
            case ShopOrder.AREA:
                holder.setText(R.id.tvArea, order.AreaName);
                if (type == 1) {
                    holder.setText(R.id.tvState, order.StateName);
                } else {
                    holder.setText(R.id.tvState, "已兑换");
                }
                break;
            case ShopOrder.GOODS:
                ShopOrder.ProductItemBean item = order.GoodItem;
                ImageLoaderUtils.displayGoods(item.getImage(), holder.getView(R.id.goods_img));
                holder.setText(R.id.goods_name, item.Title)
                        .setText(R.id.goods_price, type == 1 ? StrUtils.formatPrice(mContext, item.Price) : StrUtils.formatCoin(mContext, item.Price))
                        .setText(R.id.goods_number, "X" + item.Num);

                if (!TextUtils.isEmpty(item.Spec)) {
                    holder.setText(R.id.tvSpec, "规格：" + item.Spec);
                } else {
                    holder.setText(R.id.tvSpec, "");
                }
                holder.setText(R.id.tvStoreName, item.StoreName);
                break;
            case ShopOrder.INFO:
                holder.setVisible(R.id.tvAuto, false);
                if (type == 1) {
                    double total = order.OrderPrice + order.SubBalance;
                    if (order.RefundPrice > 0) {
                        holder.setText(R.id.tvOrderInfo, mContext.getString(R.string.goods_orders_info1, order.Num, total, order.RefundPrice));
                    } else {
                        holder.setText(R.id.tvOrderInfo, String.format(mContext.getString(R.string.goods_orders_info), order.Num, total, order.CourierPrice));
                    }
                    switch (order.OrderState) {
                        case OrdersConstants.ORDER_STATE_WAIT_PAY: //0待支付
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.btnLeft, true)
                                    .setText(R.id.btnLeft, mContext.getString(R.string.orders_state_cancel))
                                    .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener())
                                    .setVisible(R.id.btnRight, true)
                                    .setText(R.id.btnRight, mContext.getString(R.string.orders_state_pay))
                                    .setOnClickListener(R.id.btnRight, v -> gotoPay(order.OrderNo));
                            break;
                        case OrdersConstants.ORDER_STATE_PAID: //10已付款
                        case OrdersConstants.ORDER_STATE_MATCH_SENDER://20分配派送员
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.btnRight, false)
                                    .setVisible(R.id.btnLeft, true)
                                    .setText(R.id.btnLeft, mContext.getString(R.string.orders_state_cancel))
                                    .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener());

                            break;
                        case OrdersConstants.ORDER_STATE_WAIT_SENDER: //25等待派送员操作
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.btnRight, false)
                                    .setVisible(R.id.btnLeft, true)
                                    .setText(R.id.btnLeft, mContext.getString(R.string.orders_state_cancel))
                                    .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener());
                            break;
                        case OrdersConstants.ORDER_STATE_RECEIVIED_ORDER://30已接单
                        case OrdersConstants.ORDER_STATE_SENDING:  //40配送中
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.btnLeft, false)
                                    .setVisible(R.id.tvCourierInfo, true)
                                    .setText(R.id.tvCourierInfo, mContext.getString(R.string.orders_sender_phone, order.CourierName, order.CourierPhone))
                                    .setVisible(R.id.btnRight, true)
                                    .setText(R.id.btnRight, mContext.getString(R.string.contact))
                                    .setOnClickListener(R.id.btnRight, v -> PermissionHelper.call(mContext, order.CourierPhone));
                            break;
                        case OrdersConstants.ORDER_STATE_DELIVERED: //45已送达
                            double totalBackPrice = 0;
                            for (ShopOrder.ProductItemBean bean : order.ProductItem) {
                                if (bean.StoreName.equals("无货")) {
                                    totalBackPrice += bean.Price * bean.Num;
                                }
                            }
                            if (totalBackPrice > 0) {
                                holder.setText(R.id.tvOrderInfo, String.format(mContext.getString(R.string.goods_orders_info2), order.Num, total, totalBackPrice));
                            }
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.tvCourierInfo, false)
                                    .setVisible(R.id.tvCourierInfo, true)
                                    .setText(R.id.tvCourierInfo, mContext.getString(R.string.orders_sender_phone, order.CourierName, order.CourierPhone))
                                    .setVisible(R.id.btnLeft, true)
                                    .setText(R.id.btnLeft, mContext.getString(R.string.orders_state_refund))
                                    .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener())
                                    .setVisible(R.id.btnRight, true)
                                    .setText(R.id.btnRight, mContext.getString(R.string.orders_state_receive))
                                    .setOnClickListener(R.id.btnRight, new OnItemChildClickListener())
                                    .setVisible(R.id.tvAuto, true)
                                    .setText(R.id.tvAuto, mContext.getString(R.string.orders_auto, order.EndTime));
                            break;
                        case OrdersConstants.ORDER_STATE_FINISHED: //50已完成
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.tvCourierInfo, true)
                                    .setText(R.id.tvCourierInfo, mContext.getString(R.string.orders_sender_phone, order.AreaName, order.CourierPhone))
                                    .setVisible(R.id.btnRight, true)
                                    .setText(R.id.btnRight, "联  系")
                                    .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());

                            break;
                        case OrdersConstants.ORDER_STATE_CANCEL_ORDER://55已取消
                            holder.setVisible(R.id.llOperate, false);
                            break;
                        case OrdersConstants.ORDER_STATE_APPLY_REFUND://60申请退款
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.btnRight, true)
                                    .setVisible(R.id.btnLeft, false)
                                    .setVisible(R.id.tvAuto, false)
                                    .setVisible(R.id.tvCourierInfo, true)
                                    .setText(R.id.tvCourierInfo, mContext.getString(R.string.orders_sender_phone, order.CourierName, order.CourierPhone))
                                    .setText(R.id.btnRight, "放弃退款")  //放弃退款就是默认收获
                                    .setOnClickListener(R.id.btnRight, new OnItemChildClickListener());
                            break;
                        case OrdersConstants.ORDER_STATE_REJECT_REFUND: //65拒绝退款
                            if (order.IsRefund == 1) {
                                holder.setVisible(R.id.llOperate, true)
                                        .setVisible(R.id.btnLeft, true)
                                        .setText(R.id.btnLeft, "确认收货")
                                        .setVisible(R.id.tvCourierInfo, true)
                                        .setText(R.id.tvCourierInfo, mContext.getString(R.string.orders_sender_phone, order.CourierName, order.CourierPhone))
                                        .setVisible(R.id.btnRight, true)
                                        .setText(R.id.btnRight, mContext.getString(R.string.orders_state_refund))
                                        .setOnClickListener(R.id.btnRight, new OnItemChildClickListener())
                                        .setOnClickListener(R.id.btnLeft, new OnItemChildClickListener())
                                        .setVisible(R.id.tvAuto, true)
                                        .setText(R.id.tvAuto, mContext.getString(R.string.orders_auto, order.EndTime));
                            } else {
                                holder.setVisible(R.id.llOperate, false);
                            }
                            break;
                        case OrdersConstants.ORDER_STATE_REFUND_FINISHED://70退款成功
                            holder.setVisible(R.id.llOperate, true)
                                    .setVisible(R.id.btnLeft, false)
                                    .setVisible(R.id.btnRight, false)
                                    .setVisible(R.id.tvCourierInfo, true)
                                    .setText(R.id.tvCourierInfo, mContext.getString(R.string.orders_sender_phone, order.CourierName, order.CourierPhone));
                            TextView tvOrderInfo = holder.getView(R.id.tvOrderInfo);
                            tvOrderInfo.append("退款至钱包");
                            break;
                    }
                } else {
                    holder.setText(R.id.tvOrderInfo, "共" + order.Num + "件商品");
                }
                break;
        }
    }

    private void gotoPay(String orderNo) {
        Intent intent = new Intent(mContext, ConfirmOrdersActivity.class);
        intent.putExtra(ConstantValue.ORDERNO, orderNo);
        intent.putExtra(ConstantValue.TYPE, ConfirmOrdersActivity.INTENT_ORDERS_LIST);
        mContext.startActivity(intent);
    }
}
