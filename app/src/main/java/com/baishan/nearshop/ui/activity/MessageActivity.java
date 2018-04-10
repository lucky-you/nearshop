package com.baishan.nearshop.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.PushParser;
import com.baishan.nearshop.presenter.MessagePresenter;
import com.baishan.nearshop.service.PushIntentService;
import com.baishan.nearshop.ui.adapter.MessageAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IMessageView;

import java.util.ArrayList;
import java.util.List;

import static com.baishan.nearshop.service.PushIntentService.COMMON;

public class MessageActivity extends BaseMvpActivity<MessagePresenter> implements IMessageView {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<PushParser> mDatas = new ArrayList<>();
    private int type;
    private String title;


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
        title = getIntent().getStringExtra(ConstantValue.TITLE);
        initTitle(title).setRightText("清空")
                .setRightOnClickListener(v -> clear());
        type = getIntent().getIntExtra(ConstantValue.TYPE, 0);
        switch (type) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        mDatas = mvpPresenter.getAllMessage(type, user == null ? null : user.LoginToken);
        adapter = new MessageAdapter(mDatas);
        adapter.setEmptyView(new EmptyView(mContext));
        recyclerView = initCommonRecyclerView(adapter, null);
        if (type == 1) {
            //进入页面系统消息标记为已读
            for (PushParser parser : mDatas) {
                if (parser.getPushType().equals(COMMON) && !parser.getIsRead()) {
                    parser.setIsRead(true);
                    parser.update();
                }
            }
        }
    }

    private void clear() {
        if (mDatas.size() > 0) {

            new AlertDialog.Builder(this)
                    .setTitle("提示")
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
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            PushParser parser = mDatas.get(i);
            if (type != 1) {
                if (!parser.getIsRead()) {
                    parser.setIsRead(true);
                    parser.update();
                    adapter.notifyItemChanged(i);
                }
                switch (type) {
                    case 2:
                        Intent it2 = new Intent(mContext, ConfirmOrdersActivity.class);
                        it2.putExtra(ConstantValue.ORDERNO, parser.getPushValue().getOrderNo());
                        it2.putExtra(ConstantValue.TYPE, ConfirmOrdersActivity.INTENT_ORDERS_LIST);
                        startActivity(it2);
                        break;
                    case 3:
                        Intent it1 = new Intent(mContext, ServiceOrderDetailActivity.class);
                        it1.putExtra(ConstantValue.ORDERNO, parser.getPushValue().getOrderNo());
                        startActivity(it1);
                        break;
                    case 4:
                        if(parser.getPushType().equals(PushIntentService.BERETURNFORUM)){
                            Intent goIntent = new Intent(mContext, PostDetailActivity.class);
                            goIntent.putExtra(ConstantValue.POST_ID, parser.getPushValue().getLinkValue());
                            startActivity(goIntent);
                        }else{
                            Intent goIntent1 = new Intent(mContext, PostCommentDetailActivity.class);
                            goIntent1.putExtra(ConstantValue.POST_ID, parser.getPushValue().getLinkValue());
                            startActivity(goIntent1);
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.getInstance().setMessage();
    }
}


