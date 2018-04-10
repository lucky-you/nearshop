package com.baishan.nearshop.presenter;

import android.app.Activity;
import android.content.Intent;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.IPersonInfoView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class PersonInfoPresenter extends BasePresenter<IPersonInfoView> {


    public PersonInfoPresenter(IPersonInfoView mvpView) {
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

            case PhotoUtils.REQUEST_CODE_CROP_PICTURE:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                mvpView.onCropSuccess();

                break;
        }
    }

    public void uploadAvatar(String userToken,String path) {

        Map<String, RequestBody> params = new HashMap<>();
        File imgFile = new File(path);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
        params.put("userPhoto\"; filename=\"" + imgFile.getName() + "", fileBody);
        params.put("LoginToken", RequestBody.create(MediaType.parse("text/plain"), userToken));
        params.put("Method", RequestBody.create(MediaType.parse("text/plain"), "SetUserPhoto"));

        addSubscription(AppClient.getApiService().uploadAvatar(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.onUploadSuccess(response);
            }

        });
    }

    public void editUserInfo(String loginToken, String modifyType, String value) {
        addSubscription(AppClient.getApiService().editUserInfo("EditUserInfo",loginToken,modifyType,value), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                if("UserSex".equals(modifyType))
                {
                    mvpView.onEditSexSuccess(value);
                }else if("NickName".equals(modifyType))
                {
                    mvpView.onEditNickNameSuccess(value);
                }
                else if("UserAge".equals(modifyType))
                {
                    mvpView.onEditAgeSuccess(value);
                }
            }

        });
    }
}
