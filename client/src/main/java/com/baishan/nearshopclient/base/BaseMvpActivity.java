package com.baishan.nearshopclient.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.mvp.MvpActivity;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.view.TitleBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;


/**
 * Created by RayYeung on 2016/8/8.
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends MvpActivity<P> {

    protected UserInfo user;

    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        return initCommonRecyclerView(R.id.recyclerView,adapter,decoration);
    }

    public RecyclerView initCommonRecyclerView(@IdRes int id, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        RecyclerView recyclerView = (RecyclerView) findViewById(id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initGridRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public TitleBuilder initTitle(Object obj) {
        if (obj instanceof String) {
            return new TitleBuilder(this).setTitleText((String) obj);
        } else {
            return new TitleBuilder(this).setTitleText((int) obj);
        }
    }
    public TitleBuilder initRedTitle(Object obj) {
        return initTitle(obj).setTitleTextColor(getResources().getColor(R.color.white))
                .setTitleBgColor(getResources().getColor(R.color.font_red));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = BaseApplication.getInstance().getUserInfo();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        user = BaseApplication.getInstance().getUserInfo();
        super.onResume();
    }

}
