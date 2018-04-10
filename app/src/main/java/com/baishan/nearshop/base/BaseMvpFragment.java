package com.baishan.nearshop.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.mvp.MvpFragment;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.ui.activity.ForumActivity;
import com.baishan.nearshop.ui.activity.LoginActivity;
import com.baishan.nearshop.ui.activity.MyMessageActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RayYeung on 2016/8/8.
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends MvpFragment<P> {


    public boolean checkLogin() {
        getCommonData();
        if (user == null) {
            intent2Activity(LoginActivity.class);
            return false;
        }
        return true;
    }


    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initHorizontalRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        return initHorizontalRecyclerView(null, adapter, decoration);
    }

    public RecyclerView initHorizontalRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        return initHorizontalRecyclerView(recyclerView, adapter, decoration, false);
    }

    public RecyclerView initHorizontalRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, boolean reverseLayout) {
        if (recyclerView == null)
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, reverseLayout));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initGridRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        return initGridRecyclerView((RecyclerView) rootView.findViewById(R.id.recyclerView), adapter, decoration, spanCount);
    }

    public RecyclerView initGridRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getCommonData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        getCommonData();
        super.onResume();
    }

    protected Area mCurrentArea;
    protected UserInfo user;

    /**
     * 获取全局常用信息
     */
    public void getCommonData() {
        user = BaseApplication.getInstance().getUserInfo();
        mCurrentArea = BaseApplication.getInstance().getCurrentArea();
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        getCommonData();
    }

    public Map<String, Object> getCommonAreaParams() {
        Map<String, Object> params = new HashMap<>();
        if (mCurrentArea != null) {
            params.put("AreaId", mCurrentArea.AreaId);
            params.put("AdCode", mCurrentArea.AdCode);
            params.put("CityCode", mCurrentArea.CityCode);
        }
        return params;
    }

    private TextView tvNum;

    /**
     * 统一设置消息数目
     *
     * @param num
     */
    public void setMessageNum(long num) {
        if (tvNum == null) {
            View view = rootView.findViewById(R.id.tvNum);
            if(view!=null)tvNum= (TextView) view;
        }
        if (tvNum != null) {
            if (num == 0) {
                tvNum.setVisibility(View.GONE);
            } else {
                tvNum.setVisibility(View.VISIBLE);
                tvNum.setText(num + "");
            }
        }
    }


    protected void showMoreWindow(View anchor) {
        View content = View.inflate(mContext, R.layout.popup_forum_review_menu, null);
        TextView tvMessage = (TextView) content.findViewById(R.id.tvMessage);
        TextView tvForum = (TextView) content.findViewById(R.id.tvForum);
        PopupWindow popup = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setOutsideTouchable(true);
        popup.showAsDropDown(anchor);
        popup.setOnDismissListener(() ->  CommonUtil.setBackgroundAlpha(mContext,1f));
        CommonUtil.setBackgroundAlpha(mContext,0.7f);
        tvMessage.setOnClickListener(v -> {
            popup.dismiss();
            intent2Activity(MyMessageActivity.class);
        });
        tvForum.setOnClickListener(v -> {
            popup.dismiss();
            if(checkLogin())
                intent2Activity(ForumActivity.class);
        });
    }



}
