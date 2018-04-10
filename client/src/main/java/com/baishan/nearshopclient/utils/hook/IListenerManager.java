package com.baishan.nearshopclient.utils.hook;

import android.view.View;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public interface IListenerManager {
    void startListener(View v);

    boolean doListener(View v);

    void afterListener(View v);
}
