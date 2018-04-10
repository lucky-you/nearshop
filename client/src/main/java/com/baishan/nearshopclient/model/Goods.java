package com.baishan.nearshopclient.model;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class Goods {

    /**
     * ProductId : 6
     * Title : 现摘苹果
     * Price : 60.0
     * ImageUrl : http://111.47.198.193:8033/Upload/Photos/20161130/thumb_e94bc7f76bdbb997d502e36947d54ffe.jpg|http://111.47.198.193:8033/Upload/Photos/20161130/thumb_71cc4591c41a1589a9c971e89a915c47.jpg|http://111.47.198.193:8033/Upload/Photos/20161130/thumb_a0edac8664f534e08eb79277d58d3ef3.jpg
     */

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
    public String ShopToken;
    public String Title;
    public String Description;
    public String ImageUrl;
    public String Intro;
    public double Price;
    public int RowIndex;
    public int PageCount;
    public int TotalCounts;
    public int Num;


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


}
