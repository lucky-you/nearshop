package com.baishan.nearshop.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.DisplayUtils;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.mylibrary.view.flowlayout.FlowLayout;
import com.baishan.mylibrary.view.flowlayout.TagAdapter;
import com.baishan.mylibrary.view.flowlayout.TagFlowLayout;
import com.baishan.mylibrary.view.flowlayout.TagView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.ApiService;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Banner;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.OrdersParam;
import com.baishan.nearshop.model.ShopStore;
import com.baishan.nearshop.presenter.GoodsDetailPresenter;
import com.baishan.nearshop.presenter.GoodsPresenter;
import com.baishan.nearshop.ui.adapter.ImageHolderView;
import com.baishan.nearshop.ui.view.CustWebView;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.StrUtils;
import com.baishan.nearshop.view.IGoodsDetailView;
import com.baishan.nearshop.view.IGoodsView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends BaseMvpActivity<GoodsDetailPresenter> implements IGoodsDetailView, IGoodsView {

    private ConvenientBanner banner;
    private TextView tvTtitle;
    private TextView tvDesc;
    private TextView tvPrice;
    private TextView tvCoin;
    private LinearLayout llGoodsSpec;
    private TagFlowLayout specTags;
    private TextView tvNum;
    private LinearLayout llBottom;
    private LinearLayout llShopList;
    private LinearLayout llDesc;
    private TextView tvGoodsDesc;
    private CustWebView webView;
    private AlertDialog mDialog;
    private Button btnBuy;
    private Button btnAdd;
    private Button btnExchange;
    private TextView tvAddress;
    private TextView tvAddressDetail;
    private TextView tvTotalPrice;
    private ImageView ivShopcar;
    private ImageView ivBack;

    public static final int INTENT_DEFAULT = 1;
    public static final int INTENT_CATEGORY_CITY = 2;
    public static final int INTENT_BUY = 3;

    private Goods goods;
    private int goodsId;
    private GoodsPresenter goodsPresenter;
    private int goodsNum = 1;

    private Area selectedArea;
    private Address address;
    //立即购买规格
    private String spec = "";
    //购物车规格
    private String spec1 = "";

    private int type;


    @Override
    protected GoodsDetailPresenter createPresenter() {
        goodsPresenter = new GoodsPresenter(this);
        return new GoodsDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_goods_detail);
    }

    @Override
    protected void bindViews() {
        banner = (ConvenientBanner) findViewById(R.id.banner);
        tvTtitle = (TextView) findViewById(R.id.tvTtitle);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvCoin = (TextView) findViewById(R.id.tvCoin);
        llGoodsSpec = (LinearLayout) findViewById(R.id.llGoodsSpec);
        specTags = (TagFlowLayout) findViewById(R.id.specTags);
        tvNum = (TextView) findViewById(R.id.tvNum);
        llShopList = (LinearLayout) findViewById(R.id.llShopList);
        llDesc = (LinearLayout) findViewById(R.id.llDesc);
        tvGoodsDesc = (TextView) findViewById(R.id.tvGoodsDesc);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        webView = (CustWebView) findViewById(R.id.webView);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnExchange = (Button) findViewById(R.id.btnExchange);
        ivShopcar = (ImageView) findViewById(R.id.ivShopcar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //initTitle("商品详情");
        registerEvent();

        goodsId = getIntent().getIntExtra(ConstantValue.DATA, 0);
        type = getIntent().getIntExtra(ConstantValue.TYPE,INTENT_DEFAULT);
        if(type==INTENT_CATEGORY_CITY||type==INTENT_BUY){
            btnAdd.setVisibility(View.GONE);
        }
        if (goodsId == 0) {
            goods = (Goods) getIntent().getSerializableExtra(ConstantValue.GOODS);
            setGoodsInfo();
        } else {
            mvpPresenter.getGoodsInfo(goodsId);
        }
        setShopcarNum(MainActivity.getInstance().getShopCarNum());
    }

    public void setShopcarNum(int num) {
        if (num == 0) {
            tvNum.setVisibility(View.GONE);
        } else {
            tvNum.setText(num + "");
            tvNum.setVisibility(View.VISIBLE);
        }
    }

    private void setGoodsInfo() {
        if (!goods.AreaId.equals(currentArea.AreaId + "") && !goods.AreaId.equals(currentArea.AdCode) && !goods.AreaId.equals(currentArea.CityCode)) {
            android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage("该商品不在当前商区")
                    .setNegativeButton("退出", (dialog1, which) -> {
                        dialog1.dismiss();
                        finish();
                    }).setPositiveButton("切换到商品商区", (dialog1, which) -> {
                        dialog1.dismiss();
                        mvpPresenter.getAreaInfo(Integer.parseInt(goods.AreaId));
                    }).setCancelable(false).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        mvpPresenter.getShopStoreList(1, goods.Id);
        webView.loadUrl(ApiService.GOODS_DETAIL_URL + goods.ProductId + ".html");
        List<Banner> list = new ArrayList<>();
        for (String s : goods.getImages()) {
            list.add(new Banner(s));
        }
        banner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, list)
                .setPageIndicator(new int[]{R.drawable.shape_page_indicator_n, R.drawable.shape_page_indicator_f})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        tvTtitle.setText(goods.Title);
        if (!TextUtils.isEmpty(goods.Intro)) {
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(goods.Intro);
        }
        String price = "";
        if (goods.fromCoinMall) {
            btnExchange.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
            price = StrUtils.formatCoin(mContext, goods.Price);
        } else {
            price = StrUtils.formatPrice(mContext, goods.Price);
        }
        tvPrice.setText(price);
        if (goods.SCoins > 0) {
            tvCoin.setText("(送" + goods.SCoins + "S币)");
        }
        if (!TextUtils.isEmpty(goods.Description)) {
            llDesc.setVisibility(View.VISIBLE);
            tvGoodsDesc.setText(goods.Description);
        }
        if(goods.getSpec()!=null){
            llGoodsSpec.setVisibility(View.VISIBLE);
            initTag(specTags,false);
        }
        if(type==INTENT_BUY){
            showDialog();
        }
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(v -> finish());
        ivShopcar.setOnClickListener(v -> intent2Activity(ShopcarActivity.class));
        btnBuy.setOnClickListener(v -> {
            if (checkLogin() && goods != null) showDialog();
        });
        btnAdd.setOnClickListener(v -> {
            if (checkLogin() && goods != null) {
                goodsPresenter.addToShopCar(user.UserId, currentArea.defaultAddress == null ? 0 : currentArea.AreaId, goods.Id,spec1);
            }
        });
        btnExchange.setOnClickListener(v -> {
            if (checkLogin() && goods != null) showDialog();
        });
    }

    private void showDialog() {
        selectedArea = currentArea;
        if (currentArea.defaultAddress != null) address = currentArea.defaultAddress;
        if (mDialog == null) {
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
            win.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            initDialog(v);
        } else {
            mDialog.show();
        }
    }

    private void initDialog(View v) {
        RelativeLayout relAddress = (RelativeLayout) v.findViewById(R.id.relAddress);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvAddressDetail = (TextView) v.findViewById(R.id.tvAddressDetail);
        Button btnSub = (Button) v.findViewById(R.id.btnSub);
        TextView tvNum = (TextView) v.findViewById(R.id.tvNum);
        Button btnAdd = (Button) v.findViewById(R.id.btnAdd);
        tvTotalPrice = (TextView) v.findViewById(R.id.tvPrice);
        EditText etDes = (EditText) v.findViewById(R.id.etDes);
        Button btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
        TextView tvNumDesc = (TextView) v.findViewById(R.id.tvNumDesc);
        LinearLayout llRemark = (LinearLayout) v.findViewById(R.id.llRemark);
        LinearLayout llSpec = (LinearLayout) v.findViewById(R.id.llSpec);
        TagFlowLayout tags = (TagFlowLayout) v.findViewById(R.id.tags);
        if (goods.fromCoinMall) {
            tvNumDesc.setText("兑换数量");
            llRemark.setVisibility(View.GONE);
        }
        if (address != null) {
            setAddressInfo();
        }
        setTotalPrice(goods.Price);
        if (goods.getSpec() != null) {
            llSpec.setVisibility(View.VISIBLE);
            initTag(tags,true);
        }
        relAddress.setOnClickListener(v1 -> {
            Intent it = new Intent(mContext, AddrManageActivity.class);
            it.putExtra(ConstantValue.CLASSNAME, GoodsDetailActivity.class.getSimpleName());
            it.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
            startActivity(it);
        });
        btnSub.setOnClickListener(v1 -> {
            if (goodsNum > 1) {
                tvNum.setText("" + --goodsNum);
                setTotalPrice(goods.Price * goodsNum);
            }
        });
        btnAdd.setOnClickListener(v1 -> {
            tvNum.setText("" + ++goodsNum);
            setTotalPrice(goods.Price * goodsNum);
        });
        btnConfirm.setOnClickListener(v1 -> {
            if (address == null) {
                showToast((String) tvAddress.getText());
                return;
            }
            if (goods.getSpec() != null && TextUtils.isEmpty(spec)) {
                showToast("请选择商品规格");
                return;
            }
            if (goods.fromCoinMall) {
                goodsPresenter.exchangeGoods(user.UserId, currentArea.AreaId, goods.AreaProductId, address.AddressId, goodsNum);
                mDialog.dismiss();
            } else {
                String remark = etDes.getText().toString();
                OrdersParam param = new OrdersParam();
                param.UserId = user.UserId;
                param.AddressId = address.AddressId;
                param.AreaProductId = goods.Id;
                param.Num = goodsNum;
                param.Remark = remark;
                param.Spec = spec;
                Intent it = new Intent(mContext, ConfirmOrdersActivity.class);
                it.putExtra(ConstantValue.DATA, param);
                it.putExtra(ConstantValue.TYPE, ConfirmOrdersActivity.INTENT_DETAIL);
                startActivity(it);
                mDialog.dismiss();
                finish();
            }

        });
    }

    private void initTag(TagFlowLayout tags,boolean showInDialog) {
        final String[] specs = goods.getSpec();
        tags.setMaxSelectCount(1);
        tags.setAdapter(new TagAdapter<String>(specs) {

            @Override
            public View getView(FlowLayout parent, int position, String data) {
                TextView tv = new TextView(mContext);
                tv.setText(data);
                ColorStateList stateList = getResources().getColorStateList(R.color.sel_tag_text_color);
                tv.setTextColor(stateList);
                tv.setBackgroundResource(R.drawable.bg_spec_tag_common);
                tv.setGravity(Gravity.CENTER);
                int textSize  =13 ;
//                if(!showInDialog){
//                    textSize = 10;
//                }
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                int p = CommonUtil.dip2px(mContext, 10);
                if(!showInDialog){
                    p = CommonUtil.dip2px(mContext, 5);
                }
                tv.setPadding(p,p,p,p);
                tv.setTag(data);
                return tv;
            }
        });
        tags.doSelect((TagView) tags.getChildAt(0),0);
        if(showInDialog){
            spec = specs[0];
        }else{
            spec1 = specs[0];
        }
        tags.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                TagView tagView = (TagView) view;
                if (tagView.isChecked()) {
                    if(showInDialog){
                        spec = specs[position];
                    }else{
                        spec1 = specs[position];
                    }
                } else {
                    if(showInDialog){
                        spec = "";
                    }else{
                        spec1 = specs[0];
                    }
                }
                return false;
            }
        });
    }

    private void setTotalPrice(double price) {
        if (goods.fromCoinMall) {
            tvTotalPrice.setText(StrUtils.formatCoin(mContext, price));
        } else {
            tvTotalPrice.setText(StrUtils.formatPrice(mContext, price));
        }
    }

    private void setAddressInfo() {
        tvAddress.setText(selectedArea.concatPartAddress());
        tvAddressDetail.setText(address.Address + " (" + address.Contact + "收) " + address.Phone);
    }

    @Override
    public void getShopStoreListSuccess(List<ShopStore> response) {
        for (ShopStore store : response) {
            View view = View.inflate(mContext, R.layout.layout_store_info, null);
            TextView tvStoreName = (TextView) view.findViewById(R.id.tvStoreName);
            TextView tvStoreAddress = (TextView) view.findViewById(R.id.tvStoreAddress);
            tvStoreName.setText(store.StoreName);
            tvStoreAddress.setText("地址：" + store.Address);
            llShopList.addView(view);
        }
    }

    @Override
    public void getGoodsInfoSuccess(Goods goods) {
        this.goods = goods;
        setGoodsInfo();
    }

    @Override
    public void getAreaInfoSuccess(Area area) {
        showToast("切换商区成功");
        currentArea = area;
        SPUtils.set(ConstantValue.SP_AREAID, area.AreaId);
        BaseApplication.getInstance().setCurrentArea(area);
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_AREA));
    }

    @Override
    public void getAreaInfoFail(int areaId) {
        showToast("切换商区失败");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goodsPresenter != null) {
            goodsPresenter.detachView();
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
            if (GoodsDetailActivity.class.getSimpleName().equals(notice.content1)) {
                selectedArea = (Area) notice.content;
                address = selectedArea.AddressInfo.get(0);
                setAddressInfo();
            }
        } else if (notice.type == ConstantValue.MSG_TYPE_SHOPCAR_UPDATE_FINISH) {
            setShopcarNum(MainActivity.getInstance().getShopCarNum());
        }
    }
}
