package com.baishan.mylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.R;


/**
 * Created by RayYeung on 2016/8/18.
 */

/**
 * 显示进度
 */
public class EmptyView extends RelativeLayout {

    private ImageView ivHint;
    private TextView tvHint;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.layout_empty_view, this);
        ivHint = (ImageView) findViewById(R.id.ivHint);
        tvHint = (TextView) findViewById(R.id.tvHint);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public EmptyView setText(String text) {
        tvHint.setText(text);
        return this;
    }

    public EmptyView setImageResource(int resId) {
        ivHint.setImageResource(resId);
        return this;
    }

}
