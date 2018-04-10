package com.baishan.nearshop.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by RayYeung on 2016/9/20.
 */
public class Goods implements Serializable {


    /**
     * Id : 2
     * ShopToken : 1138fd04-9f04-4ddf-815e-8ab5270505c8
     * Title : 测试
     * Description : 啊啊啊
     * ImageUrl : http://bpic.588ku.com/element_banner/20/16/10/f21f6876554e210965f936aeddd84c78.jpg
     * Price : 12.0
     * RowIndex : 1
     * PageCount : 1
     * TotalCounts : 1
     */

    public int Id;
    //仅作为拼接商品详情地址
    public int ProductId;
    public String AreaId;
    public int AreaProductId;
    public String ShopToken;
    public String Title;
    public String Description;
    public String ImageUrl;
    public String Intro;
    //商品规格  |  分隔
    public String Spec;
    public double Price;
    public int RowIndex;
    public int PageCount;
    public int TotalCounts;
    /**
     * 不等于4是全市够商品
     */
    public int LinkLevel;
    public int Num;
    public int SCoins;

    public boolean fromCoinMall;

    public String[] getImages() {
        return ImageUrl.split("\\|");
    }

    public String[] getSpec() {
        if (TextUtils.isEmpty(Spec)) return null;
        return Spec.split("\\|");
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
}
