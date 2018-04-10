package com.baishan.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public abstract class LibFragment extends Fragment implements View.OnClickListener {
    protected Activity mContext;
    protected LibApplication application;
    protected boolean isFirst = true;
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return loadViewLayout(inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        application = (LibApplication) getActivity().getApplication();
        mContext = getActivity();
        rootView = view;
        initView(view);
        if (onFragmentInitFinish != null) {
            onFragmentInitFinish.onInitFinish();
        }
    }

    /**
     * 获取控件
     *
     * @param id  控件的id
     * @param <E>
     * @return
     */
    protected <E extends View> E get(int id) {
        return (E) rootView.findViewById(id);
    }

    public static interface OnFragmentInitFinish {
        void onInitFinish();
    }

    private OnFragmentInitFinish onFragmentInitFinish;

    public void setOnFragmentInitFinish(OnFragmentInitFinish onFragmentInitFinish) {
        this.onFragmentInitFinish = onFragmentInitFinish;
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    /**
     * 当界面可见时的操作
     */
    protected void onVisible() {
        if (isFirst) {
            isFirst = false;
            lazyLoad();
        }
    }

    /**
     * 数据懒加载
     */
    protected void lazyLoad() {

    }

    /**
     * 当界面不可见时的操作
     */
    protected void onInVisible() {

    }

    /**
     * 初始化界面
     *
     * @param view
     */
    private void initView(View view) {
        bindViews(view);
        processLogic();
        setListener();
    }

    /**
     * 加载布局
     */
    protected abstract View loadViewLayout(LayoutInflater inflater, ViewGroup container);

    /**
     * find控件
     *
     * @param view
     */
    protected abstract void bindViews(View view);

    /**
     * 处理数据
     */
    protected abstract void processLogic();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 界面跳转
     *
     * @param tarActivity
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(mContext, tarActivity);
        startActivity(intent);
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        ToastUtils.showToast(msg);
    }

    protected void showLog(String msg) {
        Logger.i(msg);
    }

    /**
     * 注册事件通知
     */
    public void registerEvent() {
        EventBus.getDefault().register(this);
    }

    /**
     * 发送消息
     */
    public void post(Notice msg) {
        EventBus.getDefault().post(msg);
    }


    @Override
    public void onClick(View v) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }
}
