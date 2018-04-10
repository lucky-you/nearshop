package com.baishan.nearshop.view;

import com.baishan.nearshop.model.ChatRoom;
import com.baishan.nearshop.model.ForumCategory;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
public interface IForumView {
    void getAllForumCategorySuccess(List<ForumCategory> response);

    void getChatRoomSuccess(ChatRoom chatRoom);
}
