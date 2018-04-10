package com.baishan.nearshop.presenter;

import android.app.Activity;
import android.content.Intent;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.view.IApplyPartnerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ApplyPartnerPresenter extends BasePresenter<IApplyPartnerView> {
    public ApplyPartnerPresenter(IApplyPartnerView mvpView) {
        super(mvpView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.REQUEST_CODE_FROM_ALBUM:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                mvpView.onSelectAlbumFinish(data.getData());
                break;
            case PhotoUtils.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == Activity.RESULT_CANCELED) {
                    mvpView.onCameraCancel();
                } else {
                    mvpView.onCameraFinish();
                }
                break;

        }
    }

    public void getShopAreaList(String areaCode) {
        addSubscription(AppClient.getApiService().getShopAreaList(areaCode), new SubscriberCallBack<List<Area>>() {
            @Override
            protected void onSuccess(List<Area> response) {
                mvpView.getShopAreaListSuccess(response);
            }
        });
    }

    public void applyPartner(String method, String userId, String areaString, Area selectArea, String address, String name, String phone, String cardFrontPath, String cardBackgroundPath, String licencePath) {
        Map<String, RequestBody> params = new HashMap<>();
//        File imgFile = new File(path);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
//        params.put("userPhoto\"; filename=\"" + imgFile.getName() + "", fileBody);
        String[] addresses = areaString.split(" ");
        params.put("UserId", RequestBody.create(MediaType.parse("text/plain"), userId));
        params.put("Method", RequestBody.create(MediaType.parse("text/plain"), method));
        params.put("AreaId", RequestBody.create(MediaType.parse("text/plain"), selectArea.AreaId + ""));
        params.put("AreaName", RequestBody.create(MediaType.parse("text/plain"), selectArea.AreaName));
        params.put("Province", RequestBody.create(MediaType.parse("text/plain"), addresses[0]));
        params.put("City", RequestBody.create(MediaType.parse("text/plain"), addresses[1]));
        params.put("County", RequestBody.create(MediaType.parse("text/plain"), addresses[2]));
        params.put("Address", RequestBody.create(MediaType.parse("text/plain"), address));
        params.put("Contact", RequestBody.create(MediaType.parse("text/plain"), name));
        params.put("Phone", RequestBody.create(MediaType.parse("text/plain"), phone));

        List<List<String>> imgs = new ArrayList<>();
        if ("ApplyStores ApplyServiceProvider".contains(method)) {
            //一张营业执照
            List<String> list = new ArrayList<>();
            list.add("Image1");
            list.add(licencePath);
            imgs.add(list);
        } else {
            //两张身份证
            List<String> list1 = new ArrayList<>();
            list1.add("Image1");
            list1.add(cardFrontPath);
            imgs.add(list1);

            List<String> list2 = new ArrayList<>();
            list2.add("Image2");
            list2.add(cardBackgroundPath);
            imgs.add(list2);
        }
        List<String> list = new ArrayList<>();
        list.add("ok");
        imgs.add(list);
        Observable.from(imgs).map(new Func1<List<String>, List<String>>() {
            @Override
            public List<String> call(List<String> s) {
                if (s.get(0).equals("ok")) {
                    //压缩完成了
                    return s;
                }

                File imgFile = FileUtils.getImgFile();
                PhotoUtils.compressBmpToFile(PhotoUtils.getSmallBitmap(s.get(1)), imgFile, PhotoUtils.DEFUALT_COMPRESS_SIZE);
                s.set(1, imgFile.getAbsolutePath());

//                File imgFile = new File(path);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
                params.put(s.get(0) + "\"; filename=\"" + imgFile.getName() + "", fileBody);

                return s;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> s) {
                        System.out.println(s.toString());

                        if (s.get(0).equals("ok"))
                            addSubscription(true,AppClient.getApiService().applyPartner(params), new SubscriberCallBack<String>() {
                                @Override
                                protected void onSuccess(String response) {
                                    mvpView.onApplyPartnerSuccess();
                                }
                            });
                    }
                });


    }
}
