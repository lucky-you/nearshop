package com.baishan.mylibrary.bean;

/**
 * 发送消息模板
 * Created by RayYeung on 2016/8/12.
 */
public class Notice {

    public int type;
    public Object content;
    public Object content1;

    public Notice(int type, Object content) {
        this.type = type;
        this.content = content;
    }
    public Notice(int type, Object content, Object content1) {
        this.type = type;
        this.content = content;
        this.content1=content1;
    }
    public Notice(int type) {
        this.type = type;
    }
}
