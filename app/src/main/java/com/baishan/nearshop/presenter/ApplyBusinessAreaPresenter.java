package com.baishan.nearshop.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.mylibrary.view.LoadingDialog;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.IApplyBusinessAreaView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ApplyBusinessAreaPresenter extends BasePresenter<IApplyBusinessAreaView>{
    public ApplyBusinessAreaPresenter(IApplyBusinessAreaView mvpView) {
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

    public void applyBusinessArea(String userId,String businessName, String areaString, String address, String contact, String idCard, String phone, String fixPhone, String email, String mCardFrontPath, String mCardBackgroundPath, String mLicencePath,String adCode,String cityCode) {
        Dialog dialog = LoadingDialog.show(context);
        Map<String, RequestBody> params = new HashMap<>();
        String[] addresses = areaString.split(" ");
        params.put("UserId", RequestBody.create(MediaType.parse("text/plain"), userId));
        params.put("Method", RequestBody.create(MediaType.parse("text/plain"), "ApplyAreas"));
        params.put("AreaName", RequestBody.create(MediaType.parse("text/plain"), businessName));
        params.put("Province", RequestBody.create(MediaType.parse("text/plain"), addresses[0]));
        params.put("City", RequestBody.create(MediaType.parse("text/plain"), addresses[1]));
        params.put("County", RequestBody.create(MediaType.parse("text/plain"), addresses[2]));
        params.put("Address", RequestBody.create(MediaType.parse("text/plain"), address));
        params.put("AdCode", RequestBody.create(MediaType.parse("text/plain"), adCode));
        params.put("CityCode", RequestBody.create(MediaType.parse("text/plain"), cityCode));
        params.put("Contact", RequestBody.create(MediaType.parse("text/plain"), contact));
        params.put("Phone", RequestBody.create(MediaType.parse("text/plain"), phone));
        params.put("IdNumber", RequestBody.create(MediaType.parse("text/plain"), idCard));
        params.put("Telephone", RequestBody.create(MediaType.parse("text/plain"), fixPhone));
        params.put("Email", RequestBody.create(MediaType.parse("text/plain"), email));

        List<String> imgs = new ArrayList<>();
        imgs.add(mCardFrontPath);
        imgs.add(mCardBackgroundPath);
        //imgs.add(mLicencePath);

//        Handler handler=new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//            }
//        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < imgs.size(); i++) {
                    File imgFile = FileUtils.getImgFile();
                    PhotoUtils.compressBmpToFile(PhotoUtils.getSmallBitmap(imgs.get(i)), imgFile, PhotoUtils.DEFUALT_COMPRESS_SIZE);

                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
                    params.put("Image"+(i+1) + "\"; filename=\"" + imgFile.getName() + "", fileBody);

                    if(i==imgs.size()-1)
                    {
                        addSubscription(AppClient.getApiService().applyAreas(params), new SubscriberCallBack<String>() {
                            @Override
                            protected void onSuccess(String response) {
                                dialog.dismiss();
                                mvpView.onApplyPartnerSuccess();
                            }

                            @Override
                            protected void onFailure(ResultResponse response) {
                                super.onFailure(response);
                                dialog.dismiss();
                            }

                            @Override
                            protected void onError() {
                                super.onError();
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        }).start();


    }
}
