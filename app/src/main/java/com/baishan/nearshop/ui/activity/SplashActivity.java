package com.baishan.nearshop.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.dao.UserDao;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.presenter.LocationPresenter;
import com.baishan.nearshop.presenter.SplashPresenter;
import com.baishan.nearshop.service.PushIntentService;
import com.baishan.nearshop.service.PushService;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ILocationView;
import com.baishan.nearshop.view.ISplashView;
import com.baishan.permissionlibrary.PermissionUtils;
import com.igexin.sdk.PushManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements ISplashView, ILocationView {


    private static final long WAIT_TIME = 1;
    private LocationPresenter locationPresenter;
    private AMapLocation aMapLocation;
    private String userToken;


    @Override
    protected SplashPresenter createPresenter() {
        locationPresenter = new LocationPresenter(this);
        return new SplashPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
//        Observable.timer(1, TimeUnit.SECONDS)
//                .subscribe(aLong -> {
//                    intent2Activity(MainActivity.class);
//                    finish();
//                });
        PermissionUtils.getInstance().checkMutiPermission(mContext, new PermissionUtils.PermissionCallback() {
            @Override
            public void onClose() {
                finish();
            }

            @Override
            public void onFinish() {
                PushManager.getInstance().initialize(getApplicationContext(), PushService.class);
                PushManager.getInstance().registerPushIntentService(getApplicationContext(), PushIntentService.class);
                locationPresenter.startLocation(mContext);
                //autoLogin();
            }
        });

    }

    private void autoLogin() {
        userToken = (String) SPUtils.get(ConstantValue.SP_USER_TOKEN, "");
        if (TextUtils.isEmpty(userToken)) {
            intentProcess();
//            Observable.timer(WAIT_TIME, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
//                @Override
//                public void call(Long aLong) {
//                    boolean isFirst = (boolean) SPUtils.get(ConstantValue.IS_FIRST, true);
////                    if (isFirst) {
////                        intent2Activity(GuideActivity.class);
////                        finish();
////                    } else {
//                    intentProcess();
////                    }
//                }
//            });
        } else {
            mvpPresenter.quietLogin(userToken);
        }
    }

    @Subscribe
    public void onEventMainThread(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            intentProcess();
        }
    }

    private void intentProcess() {
        if (currentArea == null) {
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage("当前位置没有商区")
                    .setNegativeButton("进入默认商区", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mvpPresenter.getAreaInfo(-1);
                        }
                    }).setPositiveButton("去选择商区", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            gotoAddr();
                        }
                    }).setCancelable(false).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            if (TextUtils.isEmpty(userToken)) {
                gotoMain();
            } else {
                int areaId = (int) SPUtils.get(ConstantValue.SP_AREAID, 0);
                if (currentArea.AreaId == areaId) {//商区没有变化
                    gotoMain();
                } else {//商区变更了
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("商区发生变化")
                            .setNegativeButton("回到上个商区", (dialog1, which) -> {
                                dialog1.dismiss();
                                mvpPresenter.getAreaInfo(areaId);
                            }).setPositiveButton("直接进入", (dialog1, which) -> {
                                dialog1.dismiss();
                                gotoMain();
                            }).setCancelable(false).create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        }
    }


    @Override
    protected void setListener() {

    }


    @Override
    public void onServerError() {
        intentProcess();
    }


    @Override
    public void onLoginFailure(ResultResponse response) {
        UserDao.logout();
        intentProcess();
    }

    @Override
    public void getUserCurrentAreaSuccess(Area response) {
        response.CityCode = aMapLocation.getCityCode();
        response.AdCode = aMapLocation.getAdCode();
        BaseApplication.getInstance().setCurrentArea(response);
        currentArea = response;
        autoLogin();
    }

    @Override
    public void getUserCurrentAreaFailure() {
        autoLogin();
    }

    @Override
    public void getAreaInfoSuccess(Area area) {
        currentArea = area;
        BaseApplication.getInstance().setCurrentArea(area);
        gotoMain();
    }

    private void gotoMain() {
        SPUtils.set(ConstantValue.SP_AREAID, currentArea.AreaId);
        Parcelable parcelableExtra = getIntent().getParcelableExtra(ConstantValue.EXTRA_INTENT);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(ConstantValue.EXTRA_INTENT, parcelableExtra);
        startActivity(intent);
        finish();
    }

    private void gotoAddr() {
        Parcelable parcelableExtra = getIntent().getParcelableExtra(ConstantValue.EXTRA_INTENT);
        Intent intent = new Intent(SplashActivity.this, SelectAddrActivity.class);
        intent.putExtra(ConstantValue.TYPE, SelectAddrActivity.INTENT_SPLASH);
        intent.putExtra(ConstantValue.EXTRA_INTENT, parcelableExtra);
        startActivity(intent);
        finish();
    }

    @Override
    public void getAreaInfoFail(int areaId) {
        if (areaId == -1) {
            gotoAddr();
        } else {
            gotoMain();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationPresenter != null) {
            locationPresenter.detachView();
        }
    }

    @Override
    public void locationSuccess(AMapLocation aMapLocation) {
        locationPresenter.stopLocation();
        this.aMapLocation = aMapLocation;
        mvpPresenter.getUserCurrentArea(aMapLocation.getAdCode(), aMapLocation.getLongitude(), aMapLocation.getLatitude());
    }

    @Override
    public void locationFailure() {
        locationPresenter.stopLocation();
        autoLogin();
    }

    @Override
    public void getCommonAreaSuccess(List<Area> response) {

    }
}
