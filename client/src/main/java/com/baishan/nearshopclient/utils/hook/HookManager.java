package com.baishan.nearshopclient.utils.hook;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class HookManager {

    private HookConfig mConfig;
    private static volatile HookManager instance;

    public static HookManager getInstance() {
        if (instance == null) {
            synchronized (HookManager.class) {
                if (instance == null) {
                    instance = new HookManager();
                }
            }
        }
        return instance;
    }


    public void hook(Activity activity, HookConfig config) {
        hook(activity.getWindow().getDecorView(), config);
    }

    public void hook(View view, HookConfig config) {
        mConfig = config;
        List<View> childs = getAllChildViews(view);
        for (View v : childs) {
            hookSingleView(v);
        }
    }

    private void hookSingleView(View v) {
        try {
            Class classView = Class.forName("android.view.View");
            Method localListenerInfoMethod = classView.getDeclaredMethod("getListenerInfo");
            localListenerInfoMethod.setAccessible(true);
            //当前View的ListenerInfo
            Object localListenerInfoObject = localListenerInfoMethod.invoke(v);

            //因为ListenerInfo是私有的，所以反射加载ListenerInfo
            Class classListenerInfo = Class.forName("android.view.View$ListenerInfo");

            Field onClickListenerFieId = classListenerInfo.getDeclaredField("mOnClickListener");
            onClickListenerFieId.setAccessible(true);
            View.OnClickListener localOnClickListener = (View.OnClickListener) onClickListenerFieId.get(localListenerInfoObject);
            if (localOnClickListener != null) //设置了点击事件才hook、
                onClickListenerFieId.set(localListenerInfoObject, new OnClickListenerProxy(localOnClickListener, mConfig.getListenerManager()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归获取所有child
     *
     * @param view
     * @return
     */
    private List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }
}
