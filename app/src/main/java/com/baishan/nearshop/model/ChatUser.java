package com.baishan.nearshop.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by RayYeung on 2017/1/12.
 */

@Entity
public class ChatUser {

    @Id
    private long userID;
    private String name;
    private String portrait;
    @Generated(hash = 789504535)
    public ChatUser(long userID, String name, String portrait) {
        this.userID = userID;
        this.name = name;
        this.portrait = portrait;
    }
    @Generated(hash = 450922767)
    public ChatUser() {
    }
    public long getUserID() {
        return this.userID;
    }
    public void setUserID(long userID) {
        this.userID = userID;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPortrait() {
        return this.portrait;
    }
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }



}
