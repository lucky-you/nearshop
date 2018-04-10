package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.ChatRoom;
import com.baishan.nearshop.model.ForumCategory;
import com.baishan.nearshop.view.IForumView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
public class ForumPresenter extends BasePresenter<IForumView> {
    public ForumPresenter(IForumView mvpView) {
        super(mvpView);
    }

    public void getCategoryForum() {
        addSubscription(AppClient.getApiService().getAllForumCategory(), new SubscriberCallBack<List<ForumCategory>>() {
            @Override
            protected void onSuccess(List<ForumCategory> response) {
//                if(response.size()>0){
                mvpView.getAllForumCategorySuccess(response);
//                }
            }
        });
    }

    public void getChatRoom(int userId, String chatRoomId, int areaId) {
        addSubscription(AppClient.getApiService().getChatRoomInfo(userId, chatRoomId, areaId), new SubscriberCallBack<ChatRoom>() {
            @Override
            protected void onSuccess(ChatRoom response) {
                if (response != null) {
                    mvpView.getChatRoomSuccess(response);
                }
            }
        });
    }
}
