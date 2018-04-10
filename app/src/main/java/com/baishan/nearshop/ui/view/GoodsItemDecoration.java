package com.baishan.nearshop.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/3/28.
 */
public class GoodsItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean hasHeader;

    public GoodsItemDecoration(int space, boolean hasHeader) {
        this.space = space;
        this.hasHeader = hasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        if (hasHeader) {
            if (position == 0) return;
            position--;
        }
        if (position % 2 == 0) {
            outRect.left = 0;
        } else {
            outRect.left = space;
        }
        if (position > 1) {
            outRect.top = space;
        }
    }
}
