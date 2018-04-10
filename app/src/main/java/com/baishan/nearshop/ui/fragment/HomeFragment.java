package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.view.VerticalSwitchLayout;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Banner;
import com.baishan.nearshop.model.FirstProduct;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.model.HotNews;
import com.baishan.nearshop.presenter.DefaultAddressPresenter;
import com.baishan.nearshop.presenter.GoodsPresenter;
import com.baishan.nearshop.presenter.HomePresenter;
import com.baishan.nearshop.ui.activity.GoodsDetailActivity;
import com.baishan.nearshop.ui.activity.GoodsListActivity;
import com.baishan.nearshop.ui.activity.MyMessageActivity;
import com.baishan.nearshop.ui.activity.ProcessCodeActivity;
import com.baishan.nearshop.ui.activity.SearchActivity;
import com.baishan.nearshop.ui.activity.SelectAddrActivity;
import com.baishan.nearshop.ui.adapter.GoodsAdapter;
import com.baishan.nearshop.ui.adapter.GridTypeAdapter;
import com.baishan.nearshop.ui.adapter.ImageHolderView;
import com.baishan.nearshop.ui.view.GoodsItemDecoration;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IDefaultAddressView;
import com.baishan.nearshop.view.IGoodsView;
import com.baishan.nearshop.view.IHomeView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class HomeFragment extends BaseMvpFragment<HomePresenter> implements IHomeView, IDefaultAddressView, IGoodsView {

    private TextView tvLocation;
    private TextView tvSearch;
    private TextView tvNum;
    private ImageView ivScan;
    private RelativeLayout relMessage;
    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;

    private View header;
    private ConvenientBanner banner;
    private RecyclerView typeList;
    private VerticalSwitchLayout switchText;
    private LinearLayout llSpecialGoods;
    private ImageView ivLeft;
    private ImageView ivTop;
    private ImageView ivBoottomLeft;
    private ImageView ivBoottomRight;
    private TextView tvLeft;
    private TextView tvTop;
    private TextView tvBoottomLeft;
    private TextView tvBoottomRight;

    private GoodsAdapter goodsAdapter;
    private GridTypeAdapter typeAdapter;
    private List<HotNews> news = new ArrayList<>();
    private List<Goods> goodsList = new ArrayList<>();
    private List<GridType> gridTypeList = new ArrayList<>();
    private List<FirstProduct> products = new ArrayList<>();
    private int currentPage = 1;
    private DefaultAddressPresenter addressPresenter;
    private GoodsPresenter goodsPresenter;

    @Override
    protected HomePresenter createPresenter() {
        addressPresenter = new DefaultAddressPresenter(this);
        goodsPresenter = new GoodsPresenter(this);
        return new HomePresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void bindViews(View view) {
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        relMessage = (RelativeLayout) view.findViewById(R.id.relMessage);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        tvNum = (TextView) view.findViewById(R.id.tvNum);
        ivScan = (ImageView) view.findViewById(R.id.ivScan);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        header = View.inflate(mContext, R.layout.header_home, null);
        banner = (ConvenientBanner) header.findViewById(R.id.banner);
        typeList = (RecyclerView) header.findViewById(R.id.typeList);
        switchText = (VerticalSwitchLayout) header.findViewById(R.id.switchText);
        llSpecialGoods = (LinearLayout) header.findViewById(R.id.llSpecialGoods);
        ivLeft = (ImageView) header.findViewById(R.id.ivLeft);
        ivTop = (ImageView) header.findViewById(R.id.ivTop);
        ivBoottomLeft = (ImageView) header.findViewById(R.id.ivBoottomLeft);
        ivBoottomRight = (ImageView) header.findViewById(R.id.ivBoottomRight);
        tvLeft = (TextView) header.findViewById(R.id.tvLeft);
        tvTop = (TextView) header.findViewById(R.id.tvTop);
        tvBoottomLeft = (TextView) header.findViewById(R.id.tvBottomLeft);
        tvBoottomRight = (TextView) header.findViewById(R.id.tvBottomRight);
    }

    @Override
    protected void processLogic() {
        registerEvent();

        goodsAdapter = new GoodsAdapter(goodsList);
        GoodsItemDecoration decoration = new GoodsItemDecoration(CommonUtil.dip2px(mContext, 5), true);
        recyclerView = initGridRecyclerView(goodsAdapter, decoration, 2);
        goodsAdapter.addHeaderView(header);
        goodsAdapter.openLoadMore(10, true);

        typeAdapter = new GridTypeAdapter(gridTypeList);
        typeList.setLayoutManager(new GridLayoutManager(mContext, 5));
        typeList.setAdapter(typeAdapter);

        setCity();

        mvpPresenter.getHotNews();
        getAreaData();
    }

    /**
     * 设置城市
     */
    private void setCity() {
        //获取当前的商区
        try {
            mCurrentArea = BaseApplication.getInstance().getCurrentArea();
            String city = mCurrentArea.City.replace("市", "");
            tvLocation.setText(city);
            if (city.length() == 3) {
                tvLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            } else if (city.length() == 4) {
                tvLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void getAreaData() {
        //获取当前的商区
        try {
            mCurrentArea = BaseApplication.getInstance().getCurrentArea();
            mvpPresenter.getBanner(getCommonAreaParams());
            mvpPresenter.getFastService(getCommonAreaParams());
            mvpPresenter.getFirstProducts(mCurrentArea.AreaId);
            mvpPresenter.getHotShopList(mCurrentArea.AreaId, currentPage = 1);
            getDefaultAddress();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setListener() {
        tvLocation.setOnClickListener(v -> intent2Activity(SelectAddrActivity.class));
        relMessage.setOnClickListener(v -> intent2Activity(MyMessageActivity.class));
        tvSearch.setOnClickListener(v -> intent2Activity(SearchActivity.class));
        ivScan.setOnClickListener(v -> intent2Activity(ProcessCodeActivity.class));
        ivLeft.setOnClickListener(v -> {
            if (products.size() > 0) gotoGoodsList(products.get(0));
        });
        ivTop.setOnClickListener(v -> {
            if (products.size() > 1) gotoGoodsList(products.get(1));
        });
        ivBoottomLeft.setOnClickListener(v -> {
            if (products.size() > 2) gotoGoodsList(products.get(2));
        });
        ivBoottomRight.setOnClickListener(v -> {
            if (products.size() > 3) gotoGoodsList(products.get(3));
        });
        goodsAdapter.setOnLoadMoreListener(() -> mvpPresenter.getHotShopList(mCurrentArea.AreaId, ++currentPage));
        goodsAdapter.setOnRecyclerViewItemClickListener((v, i) -> {
            Intent it = new Intent(mContext, GoodsDetailActivity.class);
            it.putExtra(ConstantValue.GOODS, goodsList.get(i));
            startActivity(it);

        });
        goodsAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            if (view.getId() == R.id.btnAdd) {
                if (checkLogin()) {
                    Goods goods = goodsList.get(i);
                    String spec = null;
                    if (goods.getSpec() != null) spec = goods.getSpec()[0];
                    goodsPresenter.addToShopCar(user.UserId, mCurrentArea.defaultAddress == null ? 0 : mCurrentArea.AreaId, goods.Id, spec);
                }
            }
        });
        typeAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> gridTypeList.get(i).click(mContext));
        switchText.setOnItemClickListener(index -> news.get(index).click(mContext));
        srl.setOnRefreshListener(() -> getAreaData());
    }

    private void gotoGoodsList(FirstProduct firstProduct) {
        Intent it = new Intent(mContext, GoodsListActivity.class);
        it.putExtra(ConstantValue.TITLE, firstProduct.Title);
        it.putExtra(ConstantValue.CATEGORY_TYPE, firstProduct.LinkValue);
        startActivity(it);
    }

    @Override
    public void getBannerSuccess(List<Banner> response) {
        banner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, response)
                .setPageIndicator(new int[]{R.drawable.shape_page_indicator_n, R.drawable.shape_page_indicator_f})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        banner.setOnItemClickListener(position -> response.get(position).click(mContext));
        if (response.size() > 1) {
            banner.startTurning(ConstantValue.VP_TURN_TIME);
        }
    }

    @Override
    public void getFastServiceSuccess(List<GridType> response) {
        gridTypeList.clear();
        typeAdapter.addData(response);
    }

    @Override
    public void onHotNewsSuccess(List<HotNews> response) {
        news = response;
        List<String> strs = new ArrayList<>();
        for (int i = 0; i < response.size(); i++) {
            strs.add(response.get(i).Title);
        }
        switchText.setContent(strs);
    }

    @Override
    public void onFirstProductSuccess(List<FirstProduct> response) {
        llSpecialGoods.setVisibility(response.size() > 0 ? View.VISIBLE : View.GONE);
        products = response;
        if (response.size() > 0) {
            ImageLoaderUtils.displayImage(response.get(0).ImageUrl, ivLeft);
            tvLeft.setText(response.get(0).Title);
        }
        if (response.size() > 1) {
            ImageLoaderUtils.displayImage(response.get(1).ImageUrl, ivTop);
            tvTop.setText(response.get(1).Title);
        }
        if (response.size() > 2) {
            ImageLoaderUtils.displayImage(response.get(2).ImageUrl, ivBoottomLeft);
            tvBoottomLeft.setText(response.get(2).Title);
        }
        if (response.size() > 3) {
            ImageLoaderUtils.displayImage(response.get(3).ImageUrl, ivBoottomRight);
            tvBoottomRight.setText(response.get(3).Title);
        }
    }

    @Override
    public void getHotShopListSuccess(List<Goods> response) {
        srl.setRefreshing(false);
        if (currentPage == 1) {
            goodsAdapter.getData().clear();
            goodsAdapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            goodsAdapter.notifyDataChangedAfterLoadMore(response, currentPage < response.get(0).PageCount);
        }
    }

    @Override
    public void stopRefresh() {
        srl.setRefreshing(false);
    }


    @Override
    public void getDefaultAddressSuccess(Area response) {
        mCurrentArea.defaultAddress = response.AddressInfo.get(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (addressPresenter != null) {
            addressPresenter.detachView();
        }
        if (goodsPresenter != null) {
            goodsPresenter.detachView();
        }
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            getCommonData();
            getDefaultAddress();
        } else if (notice.type == ConstantValue.MSG_TYPE_UPDATE_AREA) {
            getCommonData();
            getAreaData();
            setCity();
        }
    }

    private void getDefaultAddress() {
        if (user != null) {
            addressPresenter.getDefaultAddress(mCurrentArea.AreaId, user.UserId);
        }
    }

    @Override
    public void addToShopCarSuccess() {

    }

    @Override
    public void exchangeGoodsSuccess() {

    }

}
