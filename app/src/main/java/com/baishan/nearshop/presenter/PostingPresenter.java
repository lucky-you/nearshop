package com.baishan.nearshop.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.PostParam;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.RetrofitHelper;
import com.baishan.nearshop.utils.SensitivewordFilter;
import com.baishan.nearshop.view.IPostingView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.baishan.mylibrary.utils.ToastUtils.showToast;

/**
 * Created by RayYeung on 2017/1/9.
 */

public class PostingPresenter extends BasePresenter<IPostingView> {

    private int index;


    public PostingPresenter(IPostingView mvpView) {
        super(mvpView);
    }

    public void send(PostParam param) {
        if (TextUtils.isEmpty(param.title)) {
            showToast("标题至少要4个字喔");
            return;
        }
        if (param.title.length() < 4) {
            showToast("标题至少要4个字喔");
            return;
        }
        if (param.title.length() > 20) {
            showToast("标题最长20个字喔");
            return;
        }
        if (TextUtils.isEmpty(param.content) && param.pics.size() == 0) {
            showToast("来点内容吧");
            return;
        }
        if (!TextUtils.isEmpty(param.content)) {
            if (param.content.length() < 4) {
                showToast("内容至少要4个字喔");
                return;
            } else if (param.content.length() > ConstantValue.POST_LENGTH) {
                showToast("内容最多" + ConstantValue.POST_LENGTH + "个字喔~");
                return;
            }
        }
        if (SensitivewordFilter.getInstance(context).check(param.content)) return;


        Map<String, Object> params = new HashMap<>();
        params.put("Method", "PublishForum");
        params.put("userId", param.userId);
        params.put("userToken", param.userToken);
        params.put("categoryId", param.categoryId);
        params.put("forumTitle", param.title);
        params.put("forumContent", param.content);
        params.put("isPublisher", param.isPublisher);
        params.put("location", param.location);
        params.put("cityCode", param.cityCode);
        params.put("areaId", param.areaId);
        mvpView.showLoading();
        if (param.pics.size() > 0) {
            //图片压缩
            index = 0;
            Observable.from(param.pics)
                    .map(s -> {
                        File imgFile = FileUtils.getImgFile();
                        Bitmap bitmap = PhotoUtils.getSmallBitmap(s);
                        PhotoUtils.compressBmpToFile(bitmap, imgFile, PhotoUtils.DEFUALT_COMPRESS_SIZE);
                        return imgFile;
                    }).subscribeOn(Schedulers.newThread())
                    .subscribe(file -> {
                        params.put("file" + index, file);
                        index++;
                        if (index == param.pics.size()) {
                            send(params);
                        }
                    });
        } else {
            send(params);
        }
    }

    private void send(Map<String, Object> params) {
        addSubscription(AppClient.getApiService().post(RetrofitHelper.createMultipartParam(params)), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.hideLoading();
                showToast("发帖成功");
                context.finish();
            }

            @Override
            protected void onError() {
                super.onError();
                mvpView.hideLoading();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantValue.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    mvpView.onImageFinish(list);
                }
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
}
