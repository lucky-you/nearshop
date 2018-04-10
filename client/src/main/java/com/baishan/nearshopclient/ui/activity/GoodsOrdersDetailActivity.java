package com.baishan.nearshopclient.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.StringUtils;
import com.baishan.mylibrary.utils.ToastUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.dao.MessageDao;
import com.baishan.nearshopclient.model.Address;
import com.baishan.nearshopclient.model.SelectedStore;
import com.baishan.nearshopclient.model.SenderOrdersDetail;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.presenter.GoodsOrdersDetailPresenter;
import com.baishan.nearshopclient.ui.adapter.GoodsOrderDetailAdapter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.utils.OrdersConstants;
import com.baishan.nearshopclient.view.IGoodsOrderDetailView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Write By YeWei
 * 订单详情
 */
public class GoodsOrdersDetailActivity extends BaseMvpActivity<GoodsOrdersDetailPresenter> implements IGoodsOrderDetailView {


    private List<SenderOrdersDetail.ProductItemBean> mDatas = new ArrayList<>();

    private String orderNo;
    private GoodsOrderDetailAdapter mAdapter;

    private TextView tvConsignee;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView textOrderState;
    private TextView textOrderNum;
    private TextView textCreateOrderTime;
    private TextView textPayType;
    private TextView tvLeave;
    private TextView tvArea;
    private TextView tvTotalCount;
    private TextView tvConfirmText;
    private TextView tvGoodsDesc;
    private LinearLayout llAction;
    private TextView tvSend;
    private TextView tvIgnore;
    private LinearLayout llOperate;
    private TextView tvHint;
    private Button btnConfirm;

    private int mOrderState;
    private View mFooterView;
    private Map<String, SelectedStore> mSelectedStore = new HashMap<>();

    private String type;

    @Override
    protected GoodsOrdersDetailPresenter createPresenter() {
        return new GoodsOrdersDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_goods_orders_detail);
    }

    @Override
    protected void bindViews() {
        llAction = get(R.id.llAction);
        tvSend = get(R.id.tvSend);
        tvIgnore = get(R.id.tvIgnore);
        llOperate = (LinearLayout) findViewById(R.id.llOperate);
        tvHint = (TextView) findViewById(R.id.tvHint);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        initCommonRecyclerView(mAdapter = new GoodsOrderDetailAdapter(mDatas, mSelectedStore), null);
        View headerView = View.inflate(mContext, R.layout.layout_order_detail_header, null);
        tvConsignee = (TextView) headerView.findViewById(R.id.tvConsignee);
        tvPhone = (TextView) headerView.findViewById(R.id.tvPhone);
        tvAddress = (TextView) headerView.findViewById(R.id.tvAddress);
        textOrderState = (TextView) headerView.findViewById(R.id.textOrderState);
        textOrderNum = (TextView) headerView.findViewById(R.id.textOrderNum);
        textCreateOrderTime = (TextView) headerView.findViewById(R.id.textCreateOrderTime);
        textPayType = (TextView) headerView.findViewById(R.id.textPayType);
        tvLeave = (TextView) headerView.findViewById(R.id.tvLeave);
        tvArea = (TextView) headerView.findViewById(R.id.tvArea);
        tvTotalCount = (TextView) headerView.findViewById(R.id.tvTotalCount);
        mAdapter.addHeaderView(headerView);

        mFooterView = View.inflate(mContext, R.layout.footer_goods_order_detail, null);
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvConfirmText = (TextView) mFooterView.findViewById(R.id.tvConfirmText);
        tvGoodsDesc = (TextView) mFooterView.findViewById(R.id.tvGoodsDesc);


    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("订单详情");
        type = getIntent().getStringExtra(ConstantValue.TYPE);
        post(new Notice(ConstantValue.MSG_FINISH_GOODS_ORDERS_ACTIVITY));
        registerEvent();
        if (user.getIdentity() == UserInfo.SENDER) {
            tvGoodsDesc.setVisibility(View.GONE);
        }
        mAdapter.addFooterView(mFooterView);
        orderNo = getIntent().getStringExtra(ConstantValue.ORDER_NO);
        mvpPresenter.getGoodsOrderInfo(orderNo);
    }

    /**
     * 更新消息已读
     */
    private void updateMsgRead() {
        MessageDao.read(orderNo);
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ORDER) {
            mvpPresenter.getGoodsOrderInfo(orderNo);
        } else if (notice.type == ConstantValue.MSG_FINISH_GOODS_ORDERS_ACTIVITY) {
            finish();
        }
    }


    @Override
    protected void setListener() {

    }

    @Override
    public void getGoodsOrderInfoSuccess(SenderOrdersDetail senderOrdersDetail) {
        mOrderState = senderOrdersDetail.OrderState;
        updateMsgRead();
        if (user.getIdentity() == UserInfo.SENDER) {
            setStateInfo();
        }
        setOrderInfo(senderOrdersDetail);
        mAdapter.setOrderState(mOrderState);
        mAdapter.getData().clear();
        mAdapter.addData(senderOrdersDetail.ProductItem);
    }

    @Override
    public void changeStateSuccess(String method) {
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDER));
        if ("IgnoreShopOrder".equals(method)) {
            finish();
            return;
        }
//        switch (method) {
//            case "AcceptShopOrder":
//                mOrderState = OrdersConstants.ORDER_STATE_SENDING;
//            case "ArriveShopOrder":
//                mOrderState = OrdersConstants.ORDER_STATE_DELIVERED;
//                break;
//            case "AgreeShopRefund":
//                mOrderState = OrdersConstants.ORDER_STATE_REFUND_FINISHED;
//                break;
//            case "RefuseShopRefund":
//                mOrderState = OrdersConstants.ORDER_STATE_REJECT_REFUND;
//                break;
//        }
//
//        setStateInfo();
//        mvpPresenter.getGoodsOrderInfo(orderNo);
    }

    @Override
    public void reDistributeSuccess() {
        showToast("操作成功");
        post(new Notice(ConstantValue.MSG_UPDATE_SHOP_ORDERS));
        finish();
    }

    @Override
    public void confirmSupplySuccess() {
        showToast("操作成功");
        post(new Notice(ConstantValue.MSG_UPDATE_SHOP_ORDERS));
        finish();
    }

    private void setStateInfo() {
        if (mOrderState == OrdersConstants.ORDER_STATE_WAIT_SENDER) {
            //待操作
            llAction.setVisibility(View.VISIBLE);
            tvSend.setVisibility(View.VISIBLE);
            tvIgnore.setVisibility(user.AreaName.contains("全市购") ? View.GONE : View.VISIBLE);
            mFooterView.setVisibility(View.GONE);
            tvSend.setText("派送此单");
            tvIgnore.setText("忽略此单");
            tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    派送此单
                    if (mSelectedStore.size() != mDatas.size()) {
                        //没有选择完
                        showToast("请选择每个商品的供货商");
                        return;
                    }
                    mvpPresenter.changeState("AcceptShopOrder", mSelectedStore, orderNo, user.Id);
                }
            });
            tvIgnore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    忽略此单
                    mvpPresenter.changeState("IgnoreShopOrder", orderNo, user.Id);
                }
            });

        } else if (mOrderState == OrdersConstants.ORDER_STATE_SENDING) {
            //已接单
            mFooterView.setVisibility(View.GONE);
            tvConfirmText.setText("配送状态：配送中");
            llAction.setVisibility(View.VISIBLE);
            tvIgnore.setVisibility(View.GONE);
            tvSend.setVisibility(View.VISIBLE);
            tvSend.setText("我已送达");
            tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //我已送达
                    mvpPresenter.changeState("ArriveShopOrder", orderNo, user.Id);
                }
            });
        } else if (mOrderState == OrdersConstants.ORDER_STATE_DELIVERED) {
            //已点已送达
            mFooterView.setVisibility(View.VISIBLE);
            llAction.setVisibility(View.GONE);
            tvConfirmText.setText("配送状态：已送达");
            tvConfirmText.append(StringUtils.getColorText("请记得提醒用户确认收货喔", getResources().getColor(R.color.font_coins_blue)));
        } else if (mOrderState == OrdersConstants.ORDER_STATE_APPLY_REFUND) {
            //申请退款
            mFooterView.setVisibility(View.GONE);
            llAction.setVisibility(View.VISIBLE);
            tvIgnore.setVisibility(View.VISIBLE);
            tvSend.setVisibility(View.VISIBLE);
            tvIgnore.setText("拒绝退款");
            tvSend.setText("同意退款");
            tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //同意退款
                    new AlertDialog.Builder(mContext).setTitle("是否同意退款")
                            .setMessage("请核对无误后点击确定进行操作")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mvpPresenter.changeState("AgreeShopRefund", orderNo, user.Id);
                                }
                            })
                            .show();


                }
            });
            tvIgnore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //拒绝退款
                    mvpPresenter.changeState("RefuseShopRefund", orderNo, user.Id);
                }
            });
        } else if (mOrderState == OrdersConstants.ORDER_STATE_MATCH_SENDER) {
            mFooterView.setVisibility(View.GONE);
            llAction.setVisibility(View.VISIBLE);
            tvIgnore.setVisibility(View.GONE);
            tvSend.setVisibility(View.VISIBLE);
            tvSend.setText("抢单");
            tvSend.setOnClickListener(new View.OnClickListener() {
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
                                    //抢单
                                    mvpPresenter.changeState("RobOrder", orderNo, user.Id, 1);
                                }
                            })
                            .show();
                }
            });
        } else {
            mFooterView.setVisibility(View.GONE);
            llAction.setVisibility(View.GONE);
        }
    }

    private void setOrderInfo(SenderOrdersDetail detail) {
        Address address = detail.AddressInfo.get(0);
        tvConsignee.setText("收货人：" + address.Contact);
        tvPhone.setText("收货电话：" + address.Phone);
        tvAddress.setText("收货地址：" + detail.concatAddress() + address.Address);
        textOrderState.setText(detail.StateName);
        textOrderNum.setText(String.format(getString(R.string.order_code), detail.OrderNo));
        textCreateOrderTime.setText(String.format(getString(R.string.order_date), detail.CreateTime));
        textPayType.setText(getResources().getString(R.string.pay_type) + detail.PayType);
        tvLeave.setText("留言：" + detail.Remarks);
        tvArea.setText(detail.AreaName);
        tvTotalCount.setText("共" + detail.ProductItem.size() + "件");
//        tvConfirmText.setText(detail.AreaName);
        if (user.getIdentity() != UserInfo.SENDER) {
            tvGoodsDesc.setText(String.format(getString(R.string.goods_orders_info), detail.ProductItem.size(), detail.OrderPrice + detail.SubBalance, detail.CourierPrice));
            tvConfirmText.setText(String.format(getString(R.string.orders_sender_phone), detail.CourierName, detail.CourierPhone));
            llOperate.setVisibility(View.GONE);
            if (user.getIdentity() == UserInfo.ADMIN && detail.OrderState == OrdersConstants.ORDER_STATE_MATCH_SENDER) {
                tvHint.setText("您可以：");
                btnConfirm.setText("重新分配派送员");
                btnConfirm.setOnClickListener(v -> mvpPresenter.reDistribute(detail.OrderNo, 1));
                llOperate.setVisibility(View.VISIBLE);
            } else if (user.getIdentity() == UserInfo.BUSINESS && !TextUtils.isEmpty(type)) {
                btnConfirm.setOnClickListener(v -> mvpPresenter.confirmSupply(detail.OrderNo, user.Id));
                llOperate.setVisibility(View.VISIBLE);
            }
        }
    }


}
