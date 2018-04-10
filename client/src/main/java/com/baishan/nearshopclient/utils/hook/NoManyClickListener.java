package com.baishan.nearshopclient.utils.hook;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class NoManyClickListener implements IListenerManager {
    long[] mHits = new long[2];

    @Override
    public void startListener(View v) {
    }

    @Override
    public boolean doListener(View v) {
        //每点击一次 实现左移一格数据
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //给数组的最后赋当前时钟值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        //当0出的值大于当前时间-500时  证明在500秒内点击了2次
        return mHits[0] > SystemClock.uptimeMillis() - 500;
    }

    @Override
    public void afterListener(View v) {
    }
}
