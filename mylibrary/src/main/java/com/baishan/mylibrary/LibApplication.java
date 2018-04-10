package com.baishan.mylibrary;

import android.app.Application;

import com.baishan.mylibrary.utils.CrashHandler;
import com.baishan.mylibrary.utils.SPUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class LibApplication extends Application {

    protected static LibApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SPUtils.init(this);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(instance));
        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                instance);
        config.memoryCacheExtraOptions(480, 800);
        config.diskCacheExtraOptions(720, 1280, null);
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        if (BuildConfig.DEBUG) {
            config.writeDebugLogs(); // Remove for release app
        }
        ImageLoader.getInstance().init(config.build());
    }

    public static LibApplication getInstance() {
        return instance;
    }
}
