package com.baishan.nearshop.base;

import android.view.MotionEvent;

import com.baishan.mylibrary.LibActivity;
import com.baishan.nearshop.R;
import com.baishan.nearshop.ui.view.TitleBuilder;
import com.bugtags.library.Bugtags;

/**
 * Created by RayYeung on 2016/8/9.
 */
public abstract class BaseActivity extends LibActivity {

    public TitleBuilder initTitle(Object obj) {
        if (obj instanceof String) {
            return new TitleBuilder(this).setTitleText((String) obj);
        } else {
            return new TitleBuilder(this).setTitleText((int) obj);
        }
    }
    public TitleBuilder initRedTitle(Object obj) {
        return initTitle(obj).setLeftImage(R.drawable.ic_back_white)
                .setTextColor(getResources().getColor(R.color.white))
                .setTitleBgColor(getResources().getColor(R.color.font_red));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Bugtags.onDispatchTouchEvent(this,ev);
        return super.dispatchTouchEvent(ev);
    }
}
