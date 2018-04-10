package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.presenter.CategoryPresenter;
import com.baishan.nearshop.presenter.GoodsPresenter;
import com.baishan.nearshop.ui.activity.GoodsDetailActivity;
import com.baishan.nearshop.ui.adapter.GoodsAdapter;
import com.baishan.nearshop.ui.view.GoodsItemDecoration;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ICategoryView;
import com.baishan.nearshop.view.IGoodsView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.baishan.nearshop.utils.ConstantValue.CATEGORY_FLAG;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class CategoryFragment extends BaseMvpFragment<CategoryPresenter> implements ICategoryView, IGoodsView {

    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;
    private GoodsAdapter goodsAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private int categoryId;
    private String categoryFlag;
    private int currentPage = 1;
    private GoodsPresenter goodsPresenter;

    /**
     * 搜索结果界面
     */
    public static final int TYPE_SEARCH = 2;
    /**
     * 普通展示界面
     */
    public static final int TYPE_NORMAL = 1;
    private int mType;
    private EmptyView emptyView;
    private String keyword;

    @Override
    protected CategoryPresenter createPresenter() {
        goodsPresenter = new GoodsPresenter(this);
        return new CategoryPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        Logger.i("onCreateView");
        return inflater.inflate(R.layout.layout_refresh_recyclerview, null);
    }

    @Override
    protected void bindViews(View view) {
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }

    @Override
    protected void processLogic() {
        registerEvent();
        mType = getArguments().getInt(ConstantValue.TYPE, TYPE_NORMAL);
        if (mType == TYPE_SEARCH) {
            srl.setEnabled(false);
        }
        goodsAdapter = new GoodsAdapter(goodsList);
        emptyView = new EmptyView(mContext);
        emptyView.setText("数据加载中...");
        goodsAdapter.setEmptyView(emptyView);
        goodsAdapter.openLoadMore(10, true);
        GoodsItemDecoration decoration = new GoodsItemDecoration(CommonUtil.dip2px(mContext, 5), false);
        recyclerView = initGridRecyclerView(goodsAdapter, decoration, 2);
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_SEARCH_KEYWORD && mType == TYPE_SEARCH) {
            goodsAdapter.getData().clear();
            goodsAdapter.notifyDataSetChanged();
            keyword = (String) notice.content;
            currentPage = 1;
            getData();
        }
    }


    @Override
    protected void setListener() {
        goodsAdapter.setOnRecyclerViewItemClickListener((v, i) -> {
            Intent it = new Intent(mContext, GoodsDetailActivity.class);
            it.putExtra(ConstantValue.GOODS, goodsList.get(i));
            if (goodsList.get(i).LinkLevel != ConstantValue.CATEGORYID) {
                it.putExtra(ConstantValue.TYPE, GoodsDetailActivity.INTENT_CATEGORY_CITY);
            }
            startActivity(it);
        });
        goodsAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            if (view.getId() == R.id.btnAdd) {
                if (checkLogin()) {
                    Goods item = goodsList.get(i);
                    if (item.LinkLevel == ConstantValue.CATEGORYID) {//加购物车
                        String spec = null;
                        if (item.getSpec() != null) spec = item.getSpec()[0];
                        goodsPresenter.addToShopCar(user.UserId, mCurrentArea.defaultAddress == null ? 0 : mCurrentArea.AreaId, item.Id, spec);
                    } else {//立即购买
                        Intent it = new Intent(mContext, GoodsDetailActivity.class);
                        it.putExtra(ConstantValue.GOODS, goodsList.get(i));
                        it.putExtra(ConstantValue.TYPE, GoodsDetailActivity.INTENT_BUY);
                        startActivity(it);
                    }
                }
            }
        });
        srl.setOnRefreshListener(() -> {
            currentPage = 1;
            getData();
        });
        goodsAdapter.setOnLoadMoreListener(() -> {
            currentPage++;
            getData();
        });
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mType = getArguments().getInt(ConstantValue.TYPE, TYPE_NORMAL);
        getData();

    }

    private void getData() {
        if (mType == TYPE_NORMAL) {
            getArgs();
            Map<String, Object> params = getCommonAreaParams();
            params.put("ShopList", categoryFlag);
            params.put("CategoryId", categoryId);
            params.put("PageNow", currentPage);
            mvpPresenter.getGoodsList(params);
        } else if (mType == TYPE_SEARCH) {
            Map<String, Object> params = getCommonAreaParams();
            params.put("SearchShopList", "true");
            params.put("Keyword", keyword);
            params.put("PageNow", currentPage);
            mvpPresenter.getGoodsList(params);
        }
    }

    private void getArgs() {
        categoryId = (int) getArguments().get(ConstantValue.CATEGORY_TYPE);
        categoryFlag = (String) getArguments().get(CATEGORY_FLAG);

    }

    @Override
    public void getGoodsListSuccess(List<Goods> response) {
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
        emptyView.setText("当前分类下没有商品");
    }

    @Override
    public void addToShopCarSuccess() {

    }

    @Override
    public void exchangeGoodsSuccess() {

    }
}
