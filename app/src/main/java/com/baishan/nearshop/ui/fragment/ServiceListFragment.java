package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.presenter.ServiceListPresenter;
import com.baishan.nearshop.ui.activity.ServiceDetailActivity;
import com.baishan.nearshop.ui.adapter.EasyOrderAdapter;
import com.baishan.nearshop.ui.view.TitleBuilder;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IServiceListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by RayYeung on 2016/10/14.
 */

public class ServiceListFragment extends BaseMvpFragment<ServiceListPresenter> implements IServiceListView {

    private RecyclerView recyclerView;
    private EasyOrderAdapter mAdapter;
    private List<EasyService> data = new ArrayList<>();
    private int currentPage = 1;
    private int categoryId;
    private String title;
    private Map<String, Object> params;
    private static ServiceListFragment instance;
    private TitleBuilder builder;

    public static ServiceListFragment getInstance() {
        return instance;
    }

    @Override
    protected ServiceListPresenter createPresenter() {
        return new ServiceListPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_title_recyclerview, container, false);
    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void processLogic() {
        instance = this;
        title = (String) getArguments().get(ConstantValue.TITLE);
        builder = new TitleBuilder(mContext).setTitleText(title)
                .setLeftOnClickListener(v -> back());
        mAdapter = new EasyOrderAdapter(data);
        mAdapter.setEmptyView(new EmptyView(mContext));
        mAdapter.openLoadMore(10, true);
        recyclerView = initCommonRecyclerView(mAdapter, null);
        categoryId = Integer.parseInt((String) getArguments().get(ConstantValue.CATEGORY_TYPE));
        getData();
    }

    private void getData() {
        currentPage = 1;
        params = getCommonAreaParams();
        params.put("CategoryId", categoryId);
        params.put("PageNow", currentPage);
        mvpPresenter.getServiceList(params);
    }

    public void back() {
        ((EasyOrderFragment) getParentFragment()).remove();
    }

    @Override
    protected void setListener() {
        mAdapter.setOnRecyclerViewItemClickListener((v, i) -> {
            Intent it = new Intent(mContext, ServiceDetailActivity.class);
            it.putExtra(ConstantValue.SERVICE, data.get(i));
            startActivity(it);
        });
        mAdapter.setOnLoadMoreListener(() -> {
            params.put("PageNow", ++currentPage);
            mvpPresenter.getServiceList(params);
        });
        mAdapter.setOnReservationServiceListener(new EasyOrderAdapter.onReservationServiceListener() {
            @Override
            public void onReservationService(Area area, Address address, String remark, int serviceId) {
                mvpPresenter.reservationService(area, address, remark, user.UserId, serviceId);
            }
        });
    }

    @Override
    public void getServiceListSuccess(List<EasyService> response) {
        if (response.size() > 0) {
            mAdapter.notifyDataChangedAfterLoadMore(response, currentPage < response.get(0).PageCount);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(mAdapter))
            EventBus.getDefault().unregister(mAdapter);
        instance = null;
    }

    @Override
    public void reservationServiceSuccess(String response) {
        showToast("预约成功");
    }

    public void changeData(String title, int categoryId) {
        builder.setTitleText(title);
        data.clear();
        mAdapter.notifyDataSetChanged();
        this.categoryId = categoryId;
        getData();
    }
}
