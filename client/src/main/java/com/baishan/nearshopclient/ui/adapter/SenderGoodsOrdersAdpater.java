package com.baishan.nearshopclient.ui.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.listener.OnStateListener;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.activity.GoodsOrdersDetailActivity;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.utils.OrdersConstants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class SenderGoodsOrdersAdpater extends BaseQuickAdapter<SenderGoodsOrders> {

    public SenderGoodsOrdersAdpater(List<SenderGoodsOrders> data) {
        super(R.layout.item_order_send, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SenderGoodsOrders orders) {
        baseViewHolder.setText(R.id.tvName, "姓名：" + orders.Contact + "　　" + "电话:" + orders.Phone)
                .setText(R.id.tvAddress, "地址：" + orders.AreaName + " " + orders.Address)
                .setText(R.id.tvSendState, orders.StateName)
                .setText(R.id.tvTitle, "订单编号：" + orders.OrderNo);
        if (orders.OrderState == OrdersConstants.ORDER_STATE_WAIT_SENDER) {
            //等待派送员操作 待处理
            baseViewHolder.setVisible(R.id.llAction, true)
                    .setText(R.id.tvSend, "选供应商")
                    .setText(R.id.tvIgnore, "忽略此单")
                    .setVisible(R.id.tvIgnore, !BaseApplication.getInstance().getUserInfo().AreaName.contains("全市购"))
                    .setOnClickListener(R.id.tvSend, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //派单
                            //跳转派送员商品订单详情
                            Intent intent = new Intent(mContext, GoodsOrdersDetailActivity.class);
                            intent.putExtra(ConstantValue.ORDER_NO, orders.OrderNo);
                            mContext.startActivity(intent);
                        }
                    })
                    .setOnClickListener(R.id.tvIgnore, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //忽略此单
                            new AlertDialog.Builder(mContext).setTitle("提示")
                                    .setMessage("确定要忽略此单吗？")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (onStateListener != null)
                                                onStateListener.onState("IgnoreShopOrder", orders.OrderNo);
                                        }
                                    }).show();
                        }
                    });

        } else if (orders.OrderState == OrdersConstants.ORDER_STATE_RECEIVIED_ORDER || orders.OrderState == OrdersConstants.ORDER_STATE_SENDING) {
            baseViewHolder.setVisible(R.id.llState, true)
                    .setVisible(R.id.tvStateDesc, false)
                    .setVisible(R.id.tvSended, true)
                    .setText(R.id.tvSended, "我已送达")
                    .setOnClickListener(R.id.tvSended, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //已送达
                            if (onStateListener != null)
                                onStateListener.onState("ArriveShopOrder", orders.OrderNo);
                        }
                    });
        } else if (orders.OrderState == OrdersConstants.ORDER_STATE_DELIVERED) {
            baseViewHolder.setVisible(R.id.llState, true)
                    .setVisible(R.id.tvSended, false)
                    .setVisible(R.id.tvStateDesc, true)
                    .setText(R.id.tvStateDesc, "等待用户确认");
//        }else if(orders.OrderState == OrdersConstants.ORDER_STATE_FINISHED)
        } else if (orders.OrderState == OrdersConstants.ORDER_STATE_MATCH_SENDER) {
            baseViewHolder.setVisible(R.id.llAction, true)
                    .setVisible(R.id.tvIgnore, false)
                    .setVisible(R.id.tvSend, true)
                    .setText(R.id.tvSend, "抢单")
                    .setOnClickListener(R.id.tvSend, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                            if (userInfo != null && userInfo.IsActive == 0) {
                                ToastUtils.showToast("下班不能抢单");
                                return;
                            }
                            //抢单
                            new AlertDialog.Builder(mContext).setTitle("提示")
                                    .setMessage("是否确认抢单并在半小时内送达？")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (onStateListener != null)
                                                onStateListener.onState("RobOrder", orders.OrderNo);
                                        }
                                    }).show();

                        }
                    });
        } else {
            baseViewHolder.setVisible(R.id.llState, true);
        }
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsOrdersDetailActivity.class);
                intent.putExtra(ConstantValue.ORDER_NO, orders.OrderNo);
                mContext.startActivity(intent);
            }
        });


//        switch (index) {
//            case 0:
//                baseViewHolder.setVisible(R.id.llAction, true);
//                break;
//            case 1:
//                baseViewHolder.setVisible(R.id.llState, true)
//                        .setVisible(R.id.tvSended, true);
//                break;
//            case 2:
//                baseViewHolder.setVisible(R.id.llState, true)
//                        .setVisible(R.id.tvStateDesc, true)
//                        .setText(R.id.tvSendState, "已送达");
//                break;
//            case 3:
//                baseViewHolder.setVisible(R.id.llState, true)
//                        .setText(R.id.tvSendState, "已取消");
//                break;
//        }
    }

    private OnStateListener onStateListener;

    public void setOnStateListener(OnStateListener onStateListener) {
        this.onStateListener = onStateListener;
    }

}
