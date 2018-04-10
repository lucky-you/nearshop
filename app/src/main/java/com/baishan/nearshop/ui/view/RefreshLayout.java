package com.baishan.nearshop.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.baishan.nearshop.R;

/**
 * Created by RayYeung on 2016/12/14.
 */

public class RefreshLayout extends SwipeRefreshLayout {
    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorPrimary);
    }
}
