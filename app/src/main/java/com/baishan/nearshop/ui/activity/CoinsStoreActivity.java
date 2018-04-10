package com.baishan.nearshop.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.DisplayUtils;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.presenter.CoinsStorePresenter;
import com.baishan.nearshop.presenter.GoodsPresenter;
import com.baishan.nearshop.ui.adapter.GoodsAdapter;
import com.baishan.nearshop.ui.view.GoodsItemDecoration;
import com.baishan.nearshop.ui.view.RefreshLayout;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.StrUtils;
import com.baishan.nearshop.view.ICoinsStoreView;
import com.baishan.nearshop.view.IGoodsView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * S币商城
 */
public class CoinsStoreActivity extends BaseMvpActivity<CoinsStorePresenter> implements ICoinsStoreView, IGoodsView {

    private RecyclerView recyclerView;
    private List<Goods> data = new ArrayList<>();
    private GoodsAdapter adapter;
    private int currentPage = 1;
    private RefreshLayout srl;

    private GoodsPresenter goodsPresenter;
    private AlertDialog mDialog;

    private TextView tvAddress;
    private TextView tvAddressDetail;
    private Area selectedArea;
    private Address address;
    private Goods goods;
    private int goodsNum = 1;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_recyclerview);
    }

    @Override
    protected void bindViews() {
        initTitle("S币商城").setRightText("兑换记录")
                .setRightOnClickListener(v -> {
                    if (checkLogin())
                        intent2Activity(ExchangeRecordActivity.class);
                });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        srl = (RefreshLayout) findViewById(R.id.srl);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        adapter = new GoodsAdapter(data, GoodsAdapter.GOODS_TYPE_COINS);
        adapter.setEmptyView(new EmptyView(mContext));
        adapter.openLoadMore(10, true);
        GoodsItemDecoration decoration = new GoodsItemDecoration(CommonUtil.dip2px(mContext, 5), false);
        recyclerView = initGridRecyclerView(adapter, decoration, 2);
        mvpPresenter.getShopCoinsList(currentArea.AreaId, 1);
        selectedArea = currentArea;
        if (currentArea != null) address = currentArea.defaultAddress;
    }

    @Override
    protected void setListener() {
        //下拉刷新
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                mvpPresenter.getShopCoinsList(currentArea.AreaId, currentPage);
            }
        });
        //adapter的监听
        adapter.setOnRecyclerViewItemClickListener((v, i) -> {
            Intent it = new Intent(mContext, GoodsDetailActivity.class);
            data.get(i).fromCoinMall = true;
            it.putExtra(ConstantValue.GOODS, data.get(i));
            startActivity(it);
        });
        //加载更多
        adapter.setOnLoadMoreListener(() -> mvpPresenter.getShopCoinsList(currentArea.AreaId, ++currentPage));
        //立即兑换的监听
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            if (view.getId() == R.id.btnAdd) {
                if (checkLogin()) {
                    goods = data.get(i);
                    showDialog();
                }
            }
        });
    }

    private void showDialog() {
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.show();
        Window win = mDialog.getWindow();
        win.setWindowAnimations(R.style.dialogWindowAnim);//设置窗口弹出动画  
        win.setBackgroundDrawableResource(R.color.transparent);//设置对话框背景为透明
        WindowManager.LayoutParams params = win.getAttributes();
        params.width = DisplayUtils.getScreenWidthPixels(this);
        win.setAttributes(params);
        win.setGravity(Gravity.BOTTOM);
        View v = View.inflate(this, R.layout.popup_goods, null);
        win.setContentView(v);
        initDialog(v);
    }

    private void initDialog(View v) {
        goodsNum = 1;
        RelativeLayout relAddress = (RelativeLayout) v.findViewById(R.id.relAddress);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvAddressDetail = (TextView) v.findViewById(R.id.tvAddressDetail);
        Button btnSub = (Button) v.findViewById(R.id.btnSub);
        TextView tvNum = (TextView) v.findViewById(R.id.tvNum);
        Button btnAdd = (Button) v.findViewById(R.id.btnAdd);
        TextView tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        EditText etDes = (EditText) v.findViewById(R.id.etDes);
        Button btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
        TextView tvNumDesc = (TextView) v.findViewById(R.id.tvNumDesc);
        LinearLayout llRemark = (LinearLayout) v.findViewById(R.id.llRemark);
        tvNumDesc.setText("兑换数量");
        llRemark.setVisibility(View.GONE);
        if (address != null) {
            setAddressInfo();
        }
        tvPrice.setText(StrUtils.formatCoin(mContext, goods.Price));
        relAddress.setOnClickListener(v1 -> {
            Intent it = new Intent(mContext, AddrManageActivity.class);
            it.putExtra(ConstantValue.CLASSNAME, CoinsStoreActivity.class.getSimpleName());
            it.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
            startActivity(it);
        });
        btnSub.setOnClickListener(v1 -> {
            if (goodsNum > 1) {
                tvNum.setText("" + --goodsNum);
                tvPrice.setText(StrUtils.formatCoin(mContext, goods.Price * goodsNum));
            }
        });
        btnAdd.setOnClickListener(v1 -> {
            tvNum.setText("" + ++goodsNum);
            tvPrice.setText(StrUtils.formatCoin(mContext, goods.Price * goodsNum));
        });
        btnConfirm.setOnClickListener(v1 -> {
            if (selectedArea == null) {
                showToast((String) tvAddress.getText());
                return;
            }
            goodsPresenter.exchangeGoods(user.UserId, currentArea.AreaId, goods.AreaProductId, address.AddressId, goodsNum);
            mDialog.dismiss();
        });
    }

    private void setAddressInfo() {
        if (address != null) {
            tvAddress.setText(selectedArea.concatPartAddress());
            tvAddressDetail.setText(address.Address + " (" + address.Contact + "收) " + address.Phone);
        }
    }

    @Override
    protected CoinsStorePresenter createPresenter() {
        goodsPresenter = new GoodsPresenter(this);
        return new CoinsStorePresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goodsPresenter != null) {
            goodsPresenter.detachView();
        }
    }

    @Override
    public void getShopCoinsListSuccess(List<Goods> response) {
        srl.setRefreshing(false);
        if (currentPage == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, currentPage < response.get(0).PageCount);
        }
    }

    @Override
    public void addToShopCarSuccess() {

    }

    @Override
    public void exchangeGoodsSuccess() {

    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS) {
            if (CoinsStoreActivity.class.getSimpleName().equals(notice.content1)) {
                selectedArea = (Area) notice.content;
                address = selectedArea.AddressInfo.get(0);
                setAddressInfo();
            }
        }
    }
}
