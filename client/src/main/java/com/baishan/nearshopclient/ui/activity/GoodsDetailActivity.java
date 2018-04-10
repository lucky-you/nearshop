package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.model.Banner;
import com.baishan.nearshopclient.model.Goods;
import com.baishan.nearshopclient.model.ShopStore;
import com.baishan.nearshopclient.presenter.GoodsDetailPresenter;
import com.baishan.nearshopclient.ui.adapter.ImageHolderView;
import com.baishan.nearshopclient.ui.view.CustWebView;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.IGoodsDetailView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;
import java.util.List;


public class GoodsDetailActivity extends BaseMvpActivity<GoodsDetailPresenter> implements IGoodsDetailView {

    private ConvenientBanner banner;
    private TextView tvTtitle;
    private TextView tvDesc;
    private TextView tvPrice;
    private LinearLayout llShopList;
    private CustWebView webView;

    private Goods goods;
    private int goodsId;

    @Override
    protected GoodsDetailPresenter createPresenter() {
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
        llShopList = (LinearLayout) findViewById(R.id.llShopList);
        webView = (CustWebView) findViewById(R.id.webView);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("商品详情");
        goodsId = getIntent().getIntExtra(ConstantValue.GOODS_ID, 0);
        mvpPresenter.getGoodsInfo(goodsId);
    }


    private void setGoodsInfo() {
        mvpPresenter.getShopStoreList(1, goods.Id);
//        webView.loadUrl(ApiService.GOODS_DETAIL_URL + goods.ProductId + ".html");
        webView.loadUrl("http://111.47.198.193:8080/html/shop/7.html");
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
        tvDesc.setText(goods.Description);
        tvPrice.setText(String.format(mContext.getString(R.string.price), goods.Price));
    }

    @Override
    protected void setListener() {
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


}
