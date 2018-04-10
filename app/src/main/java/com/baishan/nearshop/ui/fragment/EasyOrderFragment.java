package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.presenter.EasyOrderPresenter;
import com.baishan.nearshop.ui.activity.SearchActivity;
import com.baishan.nearshop.ui.activity.SelectAddrActivity;
import com.baishan.nearshop.ui.activity.ServiceDetailActivity;
import com.baishan.nearshop.ui.adapter.CategoryPagerAdapter;
import com.baishan.nearshop.ui.adapter.EasyOrderAdapter;
import com.baishan.nearshop.ui.adapter.GridTypeAdapter;
import com.baishan.nearshop.ui.view.WrapContentHeightViewPager;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IEasyOrderView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/18 0018.
 */

/**
 * 便民预约
 */
public class EasyOrderFragment extends BaseMvpFragment<EasyOrderPresenter> implements IEasyOrderView {

    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;
    private RecyclerView headerRecyclerView;
    private EasyOrderAdapter mAdapter;
    private List<EasyService> data = new ArrayList<>();
    private List<GridType> headerData = new ArrayList<>();
    private GridTypeAdapter headerAdapter;
    private View headerView;
    private ServiceListFragment fragment;
    private WrapContentHeightViewPager viewPager;
    private LinearLayout llPoint;
    private int lastPos = 0;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();

    @Override
    protected EasyOrderPresenter createPresenter() {
        return new EasyOrderPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_order, null);
    }

    @Override
    protected void bindViews(View view) {
        ButterKnife.bind(this, rootView);
        recyclerView = get(R.id.recyclerView);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }

    @Override
    protected void processLogic() {
        registerEvent();
        initCommonRecyclerView(mAdapter = new EasyOrderAdapter(data), null);
        View headerView = View.inflate(getActivity(), R.layout.header_easy_order, null);
        viewPager = (WrapContentHeightViewPager) headerView.findViewById(R.id.viewPager);
        llPoint = (LinearLayout) headerView.findViewById(R.id.llPoint);
        mAdapter.openLoadMore(10, true);
//        initGridRecyclerView((RecyclerView) headerView.findViewById(R.id.headerRecyclerView), headerAdapter = new GridTypeAdapter(headerData), null, 5);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        headerAdapter = new GridTypeAdapter(headerData);

        mAdapter.addHeaderView(headerView);
        mAdapter.setOnReservationServiceListener(new EasyOrderAdapter.onReservationServiceListener() {
            @Override
            public void onReservationService(Area area, Address address, String remark, int serviceId) {
                mvpPresenter.reservationService(area, address, remark, user.UserId, serviceId);
            }
        });
        setAddress();

    }

    private void setAddress() {
        try {
            getCommonData();
            String address = mCurrentArea.County + mCurrentArea.AreaName;
            if (address.length() > 8) {
                address = mCurrentArea.AreaName;
            }
            tvAddress.setText(String.format(getString(R.string.order_address), address));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEvent(Notice msg) {
        if (msg.type == ConstantValue.MSG_TYPE_UPDATE_AREA) {
            getCommonData();
            setAddress();
            mvpPresenter.getTypeList(getCommonAreaParams());
            mvpPresenter.getRecommendServiceList(mCurrentArea.AreaId, mPageNow);
            if (ServiceListFragment.getInstance() != null) {
                remove();
            }
        } else if (msg.type == ConstantValue.MSG_TYPE_MEASURE_VIEW_PAGER) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            //重新测量
            Logger.i(w + "----" + h);
            viewPager.measure(w, h);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(mAdapter))
            EventBus.getDefault().unregister(mAdapter);
    }

    private int mPageNow = 1;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mvpPresenter.getTypeList(getCommonAreaParams());
        mvpPresenter.getRecommendServiceList(mCurrentArea.AreaId, mPageNow);
    }

    @Override
    protected void setListener() {
        tvAddress.setOnClickListener(v -> intent2Activity(SelectAddrActivity.class));
        ivSearch.setOnClickListener(v -> {
            Intent it = new Intent(mContext, SearchActivity.class);
            it.putExtra(ConstantValue.TYPE, SearchActivity.INTENT_SERVICE);
            startActivity(it);
        });
        ivMenu.setOnClickListener(v -> showMoreWindow(ivMenu));
        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            Intent it = new Intent(mContext, ServiceDetailActivity.class);
            it.putExtra(ConstantValue.SERVICE, data.get(i));
            startActivity(it);
        });
        srl.setOnRefreshListener(() -> {
            mvpPresenter.getTypeList(getCommonAreaParams());
            mvpPresenter.getRecommendServiceList(mCurrentArea.AreaId, mPageNow);
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPointViews.get(lastPos).setImageResource(R.drawable.shape_page_indicator_n_black);
                mPointViews.get(position).setImageResource(R.drawable.shape_page_indicator_f);
                lastPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getTypeListSuccess(List<GridType> response) {
        srl.setRefreshing(false);
        List<RecyclerView> views = new ArrayList<>();
        int pageCount = (int) Math.ceil(response.size() / 10.0f);
        Logger.i("pageCount:" + pageCount);
        llPoint.removeAllViews();
        mPointViews.clear();
        lastPos = 0;
        for (int i = 0; i < pageCount; i++) {
            int tem = i + 1;
            List<GridType> gridTypes = new ArrayList<>();
            if (response.size() > 10 && pageCount / tem > 1) {
                //满足10个
                int start = i * 10;
                gridTypes.addAll(response.subList(start, start + 10));
            } else {
                //不到10个
                gridTypes.addAll(response.subList((pageCount - 1) * 10, response.size()));
            }
            views.add(createRecyclerView(gridTypes));
            ImageView pointView = new ImageView(mContext);
            pointView.setPadding(5, 0, 5, 0);
            if (i == 0) {
                pointView.setImageResource(R.drawable.shape_page_indicator_f);
            } else {
                pointView.setImageResource(R.drawable.shape_page_indicator_n_black);
            }
            llPoint.addView(pointView);
            mPointViews.add(pointView);
        }
        viewPager.setOffscreenPageLimit(pageCount);
        viewPager.setAdapter(new CategoryPagerAdapter(views));
        //重新设置adapter后主动计算
        viewPager.measure(0, 0);
    }

    /**
     * @param gridTypes
     * @return
     */
    private RecyclerView createRecyclerView(List<GridType> gridTypes) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(params);
        int space = CommonUtil.dip2px(mContext, 10);
        recyclerView.setPadding(0, space, 0, 0);
        GridTypeAdapter adapter = new GridTypeAdapter(gridTypes);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i1) -> showTypeList(gridTypes.get(i1)));
        return recyclerView;
    }

    @Override
    public void getRecommendListSuccess(List<EasyService> response) {
        if (mPageNow == 1) {
            mAdapter.getData().clear();
            mAdapter.addData(response);
        }
        if (response.size() > 0 && mPageNow < response.get(0).PageCount) {
            mAdapter.notifyDataChangedAfterLoadMore(true);
        } else {
            mAdapter.notifyDataChangedAfterLoadMore(false);
        }
    }

    public void showTypeList(GridType grid) {
        if (ServiceListFragment.getInstance() == null) {
            fragment = new ServiceListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.CATEGORY_TYPE, grid.LinkValue);
            bundle.putString(ConstantValue.TITLE, grid.Title);
            fragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction().add(R.id.flContainer, fragment).commit();
        } else {
            fragment.changeData(grid.Title, Integer.parseInt(grid.LinkValue));
        }
    }


    @Override
    public void reservationServiceSuccess(String response) {
        //预约成功
        showToast("预约成功");
    }

    public void remove() {
        getChildFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void stopRefresh() {
        srl.setRefreshing(false);
    }
}
