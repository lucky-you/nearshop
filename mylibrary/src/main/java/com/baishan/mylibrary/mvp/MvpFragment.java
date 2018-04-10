package com.baishan.mylibrary.mvp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.baishan.mylibrary.LibFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MvpFragment<P extends BasePresenter> extends LibFragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mvpPresenter == null) mvpPresenter = createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void lazyLoad() {
        if (mvpPresenter == null) mvpPresenter = createPresenter();
        super.lazyLoad();
    }

    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
