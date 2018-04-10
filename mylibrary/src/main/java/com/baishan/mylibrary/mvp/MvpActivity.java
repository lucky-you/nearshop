package com.baishan.mylibrary.mvp;

import android.os.Bundle;

import com.baishan.mylibrary.LibActivity;


public abstract class MvpActivity<P extends BasePresenter> extends LibActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

}
