package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.presenter.ForumListPrensenter;
import com.baishan.nearshop.ui.activity.PostDetailActivity;
import com.baishan.nearshop.ui.adapter.PostAdapter;
import com.baishan.nearshop.ui.view.RefreshLayout;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IForumListView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class ForumListFragment extends BaseMvpFragment<ForumListPrensenter> implements IForumListView {
    private List<PostInfo> data = new ArrayList<>();
    private RefreshLayout srl;
    private PostAdapter mAdapter;

    @Override
    protected ForumListPrensenter createPresenter() {
        return new ForumListPrensenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_refresh_recyclerview, null);
    }

    @Override
    protected void bindViews(View view) {
        srl = get(R.id.srl);
    }

    @Override
    protected void processLogic() {

        mAdapter = new PostAdapter(data);
        mAdapter.openLoadMore(10, true);
        initCommonRecyclerView(mAdapter, null);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mPageNow = 1;
        getForumListData();
    }

    private int mPageNow = 1;

    /**
     * 获取帖子列表
     */
    private void getForumListData() {
        String forumType = getArguments().getString(ConstantValue.DATA_TYPE);
        int categoryId = getArguments().getInt(ConstantValue.DATA_CATEGORY);
        Map<String, String> params = new HashMap<>();
        params.put("forumlist", forumType);
        params.put("categoryid", categoryId + "");
        params.put("location", "area".equals(forumType) ? mCurrentArea.AreaId + "" : mCurrentArea.CityCode);
        params.put("pagenow", mPageNow + "");
        mvpPresenter.getForumList(srl, params);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(ConstantValue.POST_ID, data.get(i).Id + "");
                startActivity(intent);
            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNow = 1;
                getForumListData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageNow++;
                getForumListData();
            }
        });
    }

    @Override
    public void getForumListSuccess(List<PostInfo> response) {
        srl.setRefreshing(false);
        if (mPageNow == 1) {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            mAdapter.notifyDataChangedAfterLoadMore(response, mPageNow < response.get(0).PageCount);
        }
    }
}
