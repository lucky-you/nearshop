package com.baishan.nearshopclient.utils.hook;

import android.view.View;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class OnClickListenerProxy implements View.OnClickListener {

    private IListenerManager mManager;
    private View.OnClickListener mListener;

    public OnClickListenerProxy(View.OnClickListener listener, IListenerManager manager) {
        mListener = listener;
        mManager = manager;
    }

    @Override
    public void onClick(View v) {
        if (mManager != null)
            mManager.startListener(v);

        if (mManager != null && !mManager.doListener(v) && mListener != null)
            mListener.onClick(v);

        if (mManager != null)
            mManager.afterListener(v);
    }
}
