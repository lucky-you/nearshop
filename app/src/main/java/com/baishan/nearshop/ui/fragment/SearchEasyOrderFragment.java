package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.presenter.EasyOrderPresenter;
import com.baishan.nearshop.ui.activity.ServiceDetailActivity;
import com.baishan.nearshop.ui.adapter.EasyOrderAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IEasyOrderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class SearchEasyOrderFragment extends BaseMvpFragment<EasyOrderPresenter> implements IEasyOrderView {

    private RecyclerView recyclerView;
    private EasyOrderAdapter mAdapter;
    private List<EasyService> data = new ArrayList<>();

    @Override
    protected EasyOrderPresenter createPresenter() {
        return new EasyOrderPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_recyclerview, null);
    }

    @Override
    protected void bindViews(View view) {
        recyclerView = get(R.id.recyclerView);

    }

    @Override
    protected void processLogic() {
        registerEvent();
        initCommonRecyclerView(mAdapter = new EasyOrderAdapter(data), null);
        mAdapter.openLoadMore(10, true);
        mAdapter.setEmptyView(new EmptyView(mContext));

        mAdapter.setOnReservationServiceListener(new EasyOrderAdapter.onReservationServiceListener() {
            @Override
            public void onReservationService(Area area, Address address, String remark, int serviceId) {
                mvpPresenter.reservationService(area, address, remark, user.UserId, serviceId);
            }
        });

    }

    @Subscribe
    public void onEvent(Notice msg) {
        if (msg.type == ConstantValue.MSG_TYPE_SEARCH_KEYWORD) {
            String keyword = (String) msg.content;
            getCommonData();
            Map<String, Object> params = getCommonAreaParams();
            mPageNow = 1;
            params.put("SearchServiceList", "true");
            params.put("Keyword", keyword);
            params.put("PageNow", mPageNow);
            mvpPresenter.getSearchRecommendServiceList(params);
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
    protected void setListener() {
        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            Intent it = new Intent(mContext, ServiceDetailActivity.class);
            it.putExtra(ConstantValue.SERVICE, data.get(i));
            startActivity(it);
        });
    }

    @Override
    public void getTypeListSuccess(List<GridType> response) {
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


    @Override
    public void reservationServiceSuccess(String response) {
        //预约成功
        showToast("预约成功");
    }

    @Override
    public void stopRefresh() {

    }
}
