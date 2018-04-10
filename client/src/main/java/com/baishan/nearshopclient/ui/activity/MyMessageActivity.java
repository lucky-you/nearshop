package com.baishan.nearshopclient.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.db.DBManager;
import com.baishan.nearshopclient.model.PushParser;
import com.baishan.nearshopclient.presenter.MessagePresenter;
import com.baishan.nearshopclient.ui.adapter.MessageAdapter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.IMessageView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.baishan.nearshopclient.service.PushIntentService.APPLY_REFUND;
import static com.baishan.nearshopclient.service.PushIntentService.APPLY_SHOP_REFUND;
import static com.baishan.nearshopclient.service.PushIntentService.CANCEL_SHOP_ORDER;
import static com.baishan.nearshopclient.service.PushIntentService.CONFIRM_OK;
import static com.baishan.nearshopclient.service.PushIntentService.CONFIRM_OK_SHOP_ORDER;
import static com.baishan.nearshopclient.service.PushIntentService.RESERVATION_SERVICE;
import static com.baishan.nearshopclient.service.PushIntentService.SHOP_ORDER;
import static com.baishan.nearshopclient.service.PushIntentService.USER_CANCEL_ORDER;

public class MyMessageActivity extends BaseMvpActivity<MessagePresenter> implements IMessageView {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<PushParser> mDatas;


    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_recyclerview);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("我的消息").setRightText("清空")
                .setRightOnClickListener(v -> clear());
        mDatas = mvpPresenter.getMessage(user.IdentityFlag + "_" + user.Id);
        adapter = new MessageAdapter(mDatas);
        recyclerView = initCommonRecyclerView(adapter, null);
        adapter.setEmptyView(new EmptyView(mContext));
    }

    private void clear() {
        if (mDatas.size() > 0) {

            new AlertDialog.Builder(this)
                    .setMessage("是否清空全部消息?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBManager.getInstance().getPushDao().deleteInTx(mDatas);
                            mDatas.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("取消", null).show();

        }
    }

    @Override
    protected void setListener() {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                PushParser parser = mDatas.get(i);
                parser.setIsRead(true);
                parser.update();
                adapter.notifyItemChanged(i);

                Intent goIntent = null;

                switch (parser.getPushType()) {
                    case USER_CANCEL_ORDER://用户取消订单
                    case CONFIRM_OK://确认无误
                    case APPLY_REFUND://申请退款
                    case RESERVATION_SERVICE:
                        int position = -1;
                        if (parser.getPushType().equals(USER_CANCEL_ORDER)) {
                            position = 4;
                        } else if (parser.getPushType().equals(CONFIRM_OK)) {
                            position = 3;
                        } else if (parser.getPushType().equals(APPLY_REFUND)) {
                            position = 2;
                        } else if (parser.getPushType().equals(RESERVATION_SERVICE)) {
                            position = 1;
                        }
                        goIntent = new Intent(mContext, OrdersActivity.class);
                        goIntent.putExtra(ConstantValue.TYPE, OrdersActivity.ORDER_Service);
                        goIntent.putExtra(ConstantValue.POSITION, position);
                        startActivity(goIntent);
                        break;
                    case CONFIRM_OK_SHOP_ORDER://确认收货
                    case CANCEL_SHOP_ORDER://取消订单
                    case APPLY_SHOP_REFUND://申请退款
                        goIntent = new Intent(mContext, GoodsOrdersDetailActivity.class);
                        goIntent.putExtra(ConstantValue.ORDER_NO, parser.getPushValue().getOrderNo());
                        startActivity(goIntent);
                        break;
                    case SHOP_ORDER://商品来订单了
                        goIntent = new Intent(mContext, OrdersActivity.class);
                        goIntent.putExtra(ConstantValue.TYPE, OrdersActivity.ORDER_GOODS);
                        goIntent.putExtra(ConstantValue.POSITION, 1);
                        startActivity(goIntent);
                        break;

                }
            }
        });
        adapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                new AlertDialog.Builder(mContext)
                        .setMessage("是删除此消息?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatas.remove(i).delete();
                                adapter.notifyItemRemoved(i);
                            }
                        }).setNegativeButton("取消", null).show();
                return false;
            }
        });
    }
}


