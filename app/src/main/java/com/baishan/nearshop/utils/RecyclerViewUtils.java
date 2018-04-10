package com.baishan.nearshop.utils;

import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/5/16.
 */
public class RecyclerViewUtils {

    public static void pauseOrResume( RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new ScrollListListener());
    }
    /**
     * RecyclerView滑动事件
     */
    private static class ScrollListListener extends RecyclerView.OnScrollListener {

        ImageLoader imageLoader = ImageLoader.getInstance();

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            switch (newState){
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //正在滑动
                    imageLoader.pause();
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    //滑动停止
                    imageLoader.resume();
                    break;
            }
        }
    }


}
