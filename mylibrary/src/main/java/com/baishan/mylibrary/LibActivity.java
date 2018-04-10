package com.baishan.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/3/16.
 */
public abstract class LibActivity extends AppCompatActivity implements View.OnClickListener {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView(savedInstanceState);
    }

    /**
     * 初始化界面
     */
    protected void initView(Bundle savedInstanceState) {
        loadViewLayout();
        bindViews();
        processLogic(savedInstanceState);
        setListener();
    }

    protected void showLog(String log) {
        Logger.i(log);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        if(EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /**
     * 获取控件
     *
     * @param id  控件的id
     * @param <E>
     * @return
     */
    protected <E extends View> E get(int id) {
        return (E) findViewById(id);
    }

    /**
     * 加载布局
     */
    protected abstract void loadViewLayout();

    /**
     * find控件
     */
    protected abstract void bindViews();


    /**
     * 处理数据
     */
    protected abstract void processLogic(Bundle savedInstanceState);

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



}
