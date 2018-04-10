package com.baishan.nearshop.ui.adapter;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.model.Coupon;
import com.baishan.nearshop.ui.activity.AddrManageActivity;
import com.baishan.nearshop.ui.activity.CouponActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.OrdersConstants;
import com.baishan.nearshop.utils.StrUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class ConfirmOrderAdapter extends BaseMultiItemQuickAdapter<ConfirmOrderItem> {

    private BaseViewHolder mLastSelectedViewHolder;
    private int type;

    public ConfirmOrderAdapter(List<ConfirmOrderItem> data) {
        this(data, 1);
    }

    public ConfirmOrderAdapter(List<ConfirmOrderItem> data, int type) {
        super(data);
        addItemType(ConfirmOrderItem.ADDRESS, R.layout.item_confirm_order_address);
        addItemType(ConfirmOrderItem.GOODS, R.layout.item_orders_goods);
        addItemType(ConfirmOrderItem.INFO, R.layout.item_orders_goods);
        EventBus.getDefault().register(this);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ConfirmOrderItem item) {
        switch (baseViewHolder.getItemViewType()) {
            case ConfirmOrderItem.ADDRESS:
                baseViewHolder.setText(R.id.tvConsignee, "收货人：" + item.AddressInfo.get(0).Contact)
                        .setText(R.id.tvPhone, item.AddressInfo.get(0).Phone)
                        .setText(R.id.tvAddress, "收货地址：" + item.concatAddress())
                        .setText(R.id.tvAddressDetail, item.AddressInfo.get(0).Address)
                        .setText(R.id.tvArea, item.AreaName);

                break;
            case ConfirmOrderItem.GOODS:
                ImageLoaderUtils.displayGoods(item.GoodItem.getImage(), baseViewHolder.getView(R.id.goods_img));
                baseViewHolder.setVisible(R.id.llInfo, false)
                        .setVisible(R.id.rlGoods, true)
                        .setText(R.id.goods_name, item.GoodItem.Title)
                        .setText(R.id.goods_price, StrUtils.formatPrice(mContext, item.GoodItem.Price))
                        .setText(R.id.goods_number, "X" + item.GoodItem.Num);
                if (!TextUtils.isEmpty(item.GoodItem.Spec)) {
                    baseViewHolder.setText(R.id.tvSpec, "规格：" + item.GoodItem.Spec);
                } else {
                    baseViewHolder.setText(R.id.tvSpec, "");
                }
                baseViewHolder.setText(R.id.tvRefund, item.GoodItem.RefundPrice>0? ("无货退款："+new DecimalFormat("#.00").format(item.GoodItem.RefundPrice)) :"");
                baseViewHolder.setText(R.id.tvStoreName,item.GoodItem.StoreName);
                break;
            case ConfirmOrderItem.INFO:
                baseViewHolder.setVisible(R.id.llInfo, true)
                        .setVisible(R.id.rlGoods, false)
                        .setText(R.id.tvPostage, StrUtils.formatPrice(mContext, item.CourierPrice));
                if(type==1){
                    StringBuilder str = new StringBuilder();
                    str.append("(不足");
                    str.append(item.FreePostage);
                    str.append("元，收");
                    str.append(item.OldCourierPrice);
                    str.append("元派送费)");
                    baseViewHolder.setText(R.id.tvPriceHint, str.toString());
                }
                if (type == 1 || (type == 2 && item.OrderState == OrdersConstants.ORDER_STATE_WAIT_PAY)) {
                    baseViewHolder.setText(R.id.tvHasCoupon, item.HasCoupon == 1 ? "选择优惠券" : "暂无可用优惠券");
                    if (item.HasCoupon == 1) {
                        baseViewHolder.setOnClickListener(R.id.relCoupon, v -> {
                            mLastSelectedViewHolder = baseViewHolder;
                            Intent intent = new Intent(mContext, CouponActivity.class);
                            intent.putExtra(ConstantValue.TYPE, CouponActivity.INTENT_TYPE_SELECTED);
                            intent.putExtra(ConstantValue.PRICE, (int) item.OrderPrice);
                            intent.putExtra(ConstantValue.AREA_ID, item.AreaId);
                            mContext.startActivity(intent);
                        });
                    }
                } else {
                    baseViewHolder.setText(R.id.tvHasCoupon, item.CouponPrice == 0 ? "无" : StrUtils.formatPrice(mContext, item.CouponPrice))
                            .setVisible(R.id.tvMessage, false)
                            .setVisible(R.id.cod_mark, false);

                }

                if (type == 2) {
                    double total = item.OrderPrice + item.SubBalance;
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    sb.append(String.format(mContext.getString(R.string.orders_pay_hint), item.PayType, item.OrderPrice, item.SubBalance));
                    sb.append(") ");
                    sb.append(StrUtils.formatPrice1(mContext, total));
                    if(item.RefundPrice>0){
                        sb.append("\n");
                        sb.append(String.format("无货退款%.2f元至钱包",item.RefundPrice));
                    }
                    baseViewHolder.setVisible(R.id.relMoney, true)
                            .setText(R.id.tvMoney, sb.toString());
                } else {
                    baseViewHolder.setVisible(R.id.relMoney, false);
                }

                EditText cod_mark = baseViewHolder.getView(R.id.cod_mark);
                if (!TextUtils.isEmpty(item.Remarks)) {
                    cod_mark.setText(item.Remarks);
                }
                cod_mark.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        item.Remarks = s.toString();
                    }
                });
                break;
        }
        baseViewHolder.getConvertView().setOnClickListener(v -> {
            if (baseViewHolder.getItemViewType() == ConfirmOrderItem.ADDRESS) {
                if (type == 2 && item.OrderState != OrdersConstants.ORDER_STATE_WAIT_PAY)
                    return;
                //选择地址
                mLastSelectedViewHolder = baseViewHolder;

                Area area = new Area();
                area.AreaId = item.AreaId;
                area.City = item.City;
                area.Province = item.Province;
                area.County = item.County;
                area.AreaName = item.AreaName;

                Intent intent = new Intent(mContext, AddrManageActivity.class);
                intent.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
                intent.putExtra(ConstantValue.AREA, area);

                mContext.startActivity(intent);
            } else if (baseViewHolder.getItemViewType() == ConfirmOrderItem.GOODS) {

            }
        });
    }

    @Subscribe
    public void onEvent(Notice msg) {

        if (msg.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS) {
            if (mLastSelectedViewHolder == null) return;
            int viewHolderPosition = getViewHolderPosition(mLastSelectedViewHolder);
            ConfirmOrderItem item = (ConfirmOrderItem) mData.get(viewHolderPosition);

            //选择地址
            Area area = (Area) msg.content;
            item.AddressInfo = area.AddressInfo;
            notifyItemChanged(viewHolderPosition);
            EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_SELECTED_ADDRESS_FROM_ADAPTER, item));
        } else if (msg.type == ConstantValue.MSG_TYPE_SELECTED_COUPON) {
            if (mLastSelectedViewHolder == null) return;
            int viewHolderPosition = getViewHolderPosition(mLastSelectedViewHolder);
            ConfirmOrderItem item = (ConfirmOrderItem) mData.get(viewHolderPosition);

            //选择优惠券
            Coupon coupon = (Coupon) msg.content;
            mLastSelectedViewHolder.setText(R.id.tvHasCoupon, "￥" + coupon.Price);
            EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_SELECTED_COUPON_FROM_ADAPTER, coupon, item));
        }
    }

}
