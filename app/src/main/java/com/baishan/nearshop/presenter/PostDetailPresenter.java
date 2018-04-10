package com.baishan.nearshop.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.PostCommentParser;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.PostParam;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.RetrofitHelper;
import com.baishan.nearshop.utils.SensitivewordFilter;
import com.baishan.nearshop.view.IPostDetailView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.baishan.mylibrary.utils.ToastUtils.showToast;

/**
 * Created by Administrator on 2017/1/11 0011.
 */
public class PostDetailPresenter extends BasePresenter<IPostDetailView>{
    private int index;

    public PostDetailPresenter(IPostDetailView mvpView) {
        super(mvpView);
    }

    public void getPostDetail(Map<String, String> params) {
        addSubscription(AppClient.getApiService().getPostDetail(params), new SubscriberCallBack<List<PostInfo>>() {
            @Override
            protected void onSuccess(List<PostInfo> response) {
                mvpView.getPostDetailSuccess(response);
            }
        });
    }
    public void getCommentData(Map<String, String> params) {
        addSubscription(AppClient.getApiService().getCommentData(params), new SubscriberCallBack<PostCommentParser>() {
            @Override
            protected void onSuccess(PostCommentParser response) {
                mvpView.getCommentDataSuccess(response);
            }
        });
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
        params.put("parentId", param.parentId);
        params.put("parentToken", param.parentToken);
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
                        params.put("file"+index,file);
                        index++;
                        if(index==param.pics.size()){
                            send(params);
                        }
                    });
        }else{
            send(params);
        }
    }

    private void send(Map<String, Object> params) {
        addSubscription(AppClient.getApiService().post(RetrofitHelper.createMultipartParam(params)), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.hideLoading();
                mvpView.onSendCommentSuccess(response);

            }

            @Override
            protected void onError() {
                super.onError();
                mvpView.hideLoading();
            }
        });
    }

    public void getRewardData(int forumid) {
        addSubscription(AppClient.getApiService().getRewardData(forumid+""), new SubscriberCallBack<List<UserInfo>>() {
            @Override
            protected void onSuccess(List<UserInfo> response) {
                mvpView.getRewardDataSuccess(response);
            }

        });
    }

    public void report(Map<String, String> params) {
        addSubscription(AppClient.getApiService().postBBS(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.onReportSuccess();
            }

        });
    }
}
