package com.baishan.nearshopclient.ui.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.StringUtils;
import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.mylibrary.view.IosDialog;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.listener.OnStateListener;
import com.baishan.nearshopclient.model.ServiceOrders;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.view.CommonDialog;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class ServiceOrdersAdaper extends BaseQuickAdapter<ServiceOrders> {

    private int index;

    public ServiceOrdersAdaper(List<ServiceOrders> data, int index) {
        super(R.layout.item_order_service, data);
        this.index = index;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ServiceOrders serviceOrders) {
        ImageLoaderUtils.displayImage(serviceOrders.ImageUrl.split("\\|")[0], baseViewHolder.getView(R.id.ivPic));
        baseViewHolder.setText(R.id.tvArea, serviceOrders.AreaName)
                .setText(R.id.tvTitle, serviceOrders.Title)
                .setText(R.id.tvPrice, "￥" + serviceOrders.Price)
                .setText(R.id.tvCompany, serviceOrders.Name)
                .setText(R.id.tvState, serviceOrders.StateName);
        if (serviceOrders.OrderState == ConstantValue.ORDER_STATE_RESERVED) {
            //已预约
            baseViewHolder.setVisible(R.id.llServiceBottom, true)
                    .setText(R.id.tv1, "拒绝此单")
                    .setVisible(R.id.tv1, !BaseApplication.getInstance().getUserInfo().AreaName.contains("全市购"))
                    .setText(R.id.tv2, "接受此单")
                    .setOnClickListener(R.id.tv1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onStateListener != null)
                                onStateListener.onState("IgnoreOrder", serviceOrders.OrderNo);
                        }
                    })
                    .setOnClickListener(R.id.tv2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onStateListener != null)
                                onStateListener.onState("AcceptOrder", serviceOrders.OrderNo);
                        }
                    });
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrderDialog(serviceOrders, "联系用户", "我要接单");
                }
            });

        } else if (serviceOrders.OrderState == ConstantValue.ORDER_STATE_RECEIVIED_ORDER) {
            //已接单
            baseViewHolder.setVisible(R.id.llServiceBottom, true)
                    .setText(R.id.tv1, "取消订单")
                    .setText(R.id.tv2, "开始工作")
                    .setOnClickListener(R.id.tv1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new CommonDialog(mContext).setTitle("取消订单")
                                    .setCustomView(View.inflate(mContext, R.layout.dialog_order_cancel, null))
                                    .setButton("是的，我要取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (onStateListener != null)
                                                onStateListener.onState("CancelOrder", serviceOrders.OrderNo);
                                        }
                                    }).show();
                        }
                    })
                    .setOnClickListener(R.id.tv2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new CommonDialog(mContext).setTitle("开始工作")
                                    .setCustomView(View.inflate(mContext, R.layout.dialog_order_start_work, null))
                                    .setButton("是的，我已开始工作", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (onStateListener != null)
                                                onStateListener.onState("StartWork", serviceOrders.OrderNo);
                                        }
                                    }).show();
                        }
                    });
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrderDialog(serviceOrders, "已接单", "");
                }
            });
        } else if (serviceOrders.OrderState == ConstantValue.ORDER_STATE_START_WORK) {
            //开始工作
            baseViewHolder.setVisible(R.id.llServiceBottom, false)
                    .getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrderDialog(serviceOrders, "订单信息", "完成工作");
                }
            });
        } else if (serviceOrders.OrderState == ConstantValue.ORDER_STATE_APPLY_REFUND) {
            //申请退款
            baseViewHolder.setVisible(R.id.llServiceBottom, true)
                    .setText(R.id.tv1, "拒绝退款")
                    .setText(R.id.tv2, "同意退款")
                    .setOnClickListener(R.id.tv1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onStateListener != null)
                                onStateListener.onState("RefuseRefund", serviceOrders.OrderNo);
                        }
                    })
                    .setOnClickListener(R.id.tv2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onStateListener != null)
                                onStateListener.onState("AgreeRefund", serviceOrders.OrderNo);
                        }
                    });

        } else if (serviceOrders.OrderState == ConstantValue.ORDER_STATE_MATCH_SENDER) {
            //分配派送员
            baseViewHolder.setVisible(R.id.llServiceBottom, true)
                    .setVisible(R.id.tv1, false)
                    .setText(R.id.tv2, "抢  单")
                    .setOnClickListener(R.id.tv2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                            if (userInfo != null && userInfo.IsActive == 0) {
                                ToastUtils.showToast("下班不能抢单");
                                return;
                            }
                            //抢单
                            new AlertDialog.Builder(mContext).setTitle("提示")
                                    .setMessage("是否确认抢单")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (onStateListener != null)
                                                onStateListener.onState("RobOrder", serviceOrders.OrderNo);
                                        }
                                    })
                                    .show();
                        }
                    })
                    .getConvertView()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOrderDialog(serviceOrders, "抢单", "我要抢单");
                        }
                    });
        } else if (serviceOrders.OrderState > ConstantValue.ORDER_STATE_START_WORK) {
            //开始工作
            baseViewHolder.setVisible(R.id.llServiceBottom, false).getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrderDialog(serviceOrders, "订单信息", "");
                }
            });
        }

    }

    private void showOrderDialog(ServiceOrders orders, String title, String btnText) {

        View content = View.inflate(mContext, R.layout.dialog_order, null);

        TextView tvAreaName = (TextView) content.findViewById(R.id.tvAreaName);
        TextView tvAddress = (TextView) content.findViewById(R.id.tvAddress);

        LinearLayout llWorkInfo = (LinearLayout) content.findViewById(R.id.llWorkInfo);
        TextView tvWorkState = (TextView) content.findViewById(R.id.tvWorkState);
        TextView tvcrerateTime = (TextView) content.findViewById(R.id.crerateOrderTime);
        TextView tvPayState = (TextView) content.findViewById(R.id.tvPayState);
        TextView tvPayPrice = (TextView) content.findViewById(R.id.tvPayPrice);

        EditText etRemark = (EditText) content.findViewById(R.id.etRemark);

        tvAreaName.setText(orders.AreaName);
        tvAddress.setText(orders.Address + " (" + orders.Contact + " 收" + ") " + orders.Phone);
        tvcrerateTime.setText("下单时间" + orders.CreateTime);
        etRemark.setText(orders.Remarks);
        etRemark.setEnabled(false);
        if (orders.OrderState == ConstantValue.ORDER_STATE_RESERVED) {
            llWorkInfo.setVisibility(View.GONE);
        } else {
            llWorkInfo.setVisibility(View.VISIBLE);
            tvWorkState.setText("工作状态:" + orders.StateName);
            if (orders.OrderPrice == 0) {
                tvPayState.setText("付款状态:未付款");
                tvPayState.append(StringUtils.getColorText("请记得提醒客户付款喔！", mContext.getResources().getColor(R.color.font_coins_blue)));
                tvPayPrice.setVisibility(View.GONE);
            } else {
                tvPayState.setText("付款状态:已付款");
                tvPayPrice.setVisibility(View.VISIBLE);
                tvPayPrice.setText("实付金额:" + orders.OrderPrice);
            }


        }

        CommonDialog commonDialog = new CommonDialog(mContext)
                .setCustomView(content)
                .setTitle(title);
        if (!TextUtils.isEmpty(btnText)) {
            commonDialog.setButton(btnText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orders.OrderState == ConstantValue.ORDER_STATE_RESERVED) {
                        //接单
                        if (onStateListener != null)
                            onStateListener.onState("IgnoreOrder", orders.OrderNo);
                    } else if (orders.OrderState == ConstantValue.ORDER_STATE_MATCH_SENDER) {
                        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                        if (userInfo != null && userInfo.IsActive == 0) {
                            ToastUtils.showToast("下班不能抢单");
                            return;
                        }
                        //抢单
                        if (onStateListener != null)
                            onStateListener.onState("RobOrder", orders.OrderNo);
                    } else {
                        //完成工作
                        IosDialog dialog = new IosDialog(mContext).builder();
                        dialog.setTitle("备注")
                                .setEditHint("需要给用户说点什么...")
                                .setNegativeButton(null, null)
                                .setPositiveButton(null, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (commonDialog != null && commonDialog.isShowing())
                                            commonDialog.dismiss();
                                        if (dialog != null && dialog.isShowing())
                                            dialog.dismiss();

                                        String remark = dialog.getResult();
                                        if (onStateListener != null)
                                            onStateListener.onState("FinishWork", orders.OrderNo, remark);
                                    }
                                }).show();

                    }
                }
            });

        }
        commonDialog.show();

    }


    private OnStateListener onStateListener;

    public void setOnStateListener(OnStateListener onStateListener) {
        this.onStateListener = onStateListener;
    }

}
