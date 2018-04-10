package com.baishan.nearshop.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class EasyService implements Serializable {
    /**
     * Id : 1
     * ServiceToken : ed7d1e82-cddb-45ae-beea-389094d4bc6d
     * ImageUrl : http://bpic.588ku.com/element_banner/20/16/10/f21f6876554e210965f936aeddd84c78.jpg
     * Title : 测试服务
     * Description : 大法师
     * Price : 见面再谈
     * Name : 58同城
     * Intro : 来呀来呀
     * Phone : 15717164545
     * ProviderImage : http://bpic.588ku.com/element_banner/20/16/10/f21f6876554e210965f936aeddd84c78.jpg
     * HasLicense : 1
     * HasIdentity : 1
     * RowIndex : 1
     * PageCount : 1
     * TotalCounts : 1
     */

    public int Id;
    public int ServiceId;
    public String ServiceToken;
    public String ServiceIntro;
    public String ImageUrl;
    public String Title;
    public String Description;
    public String Price;
    public String Name;
    public String Intro;
    public String Phone;
    public String ProviderImage;
    public int HasLicense;
    public int HasIdentity;
    public int RowIndex;
    public int PageCount;
    public int TotalCounts;
    //保证金
    public double Deposit;

    public String[] getImages() {
        return ImageUrl.split("\\|");
    }

    public String getImage() {
        if (ImageUrl == null) {
            return null;
        }
        String[] urls = ImageUrl.split("\\|");
        if (urls.length > 0) {
            return urls[0];
        }
        return null;
    }

    public List<ImageInfo> getProviderImage() {
        List<ImageInfo> list = new ArrayList<>();
        if (TextUtils.isEmpty(ProviderImage)) {
            return list;
        }
        list = new Gson().fromJson(ProviderImage, new TypeToken<List<ImageInfo>>() {
        }.getType());
        return list;
    }
}
