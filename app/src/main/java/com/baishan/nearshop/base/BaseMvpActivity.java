package com.baishan.nearshop.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.mvp.MvpActivity;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.ui.activity.LoginActivity;
import com.baishan.nearshop.ui.view.TitleBuilder;
import com.bugtags.library.Bugtags;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by RayYeung on 2016/8/8.
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends MvpActivity<P> {

    protected UserInfo user;
    protected Area currentArea;

    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        return initCommonRecyclerView(R.id.recyclerView, adapter, decoration);
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

    public RecyclerView initGridRecyclerView(@IdRes int id, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        RecyclerView recyclerView = (RecyclerView) findViewById(id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initGridRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        return initGridRecyclerView(R.id.recyclerView, adapter, decoration, spanCount);
    }

    public TitleBuilder initTitle(Object obj) {
        if (obj instanceof String) {
            return new TitleBuilder(this).setTitleText((String) obj);
        } else {
            return new TitleBuilder(this).setTitleText((int) obj);
        }
    }

    public TitleBuilder initRedTitle(Object obj) {
        return initTitle(obj).setLeftImage(R.drawable.ic_back_white)
                .setTitleTextColor(getResources().getColor(R.color.white))
                .setTitleBgColor(getResources().getColor(R.color.font_red));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getCommonData();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
       getCommonData();
        super.onResume();
        Bugtags.onResume(this);
    }

    /**
     * 获取全局常用信息
     */
    public void getCommonData() {
        user = BaseApplication.getInstance().getUserInfo();
        currentArea = BaseApplication.getInstance().getCurrentArea();
    }

    public boolean checkLogin() {
        getCommonData();
        if (user == null) {
            intent2Activity(LoginActivity.class);
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Bugtags.onDispatchTouchEvent(this,ev);
        return super.dispatchTouchEvent(ev);
    }

}
