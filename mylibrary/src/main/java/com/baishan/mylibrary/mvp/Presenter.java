package com.baishan.mylibrary.mvp;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
