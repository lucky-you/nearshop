package com.baishan.nearshop.model;

import android.content.Context;
import android.content.Intent;

import com.baishan.nearshop.ui.activity.GoodsDetailActivity;
import com.baishan.nearshop.ui.activity.GoodsListActivity;
import com.baishan.nearshop.ui.activity.MainActivity;
import com.baishan.nearshop.ui.activity.ServiceDetailActivity;
import com.baishan.nearshop.ui.activity.WebActivity;
import com.baishan.nearshop.ui.fragment.EasyOrderFragment;
import com.baishan.nearshop.ui.fragment.FragmentController;
import com.baishan.nearshop.ui.fragment.SupermarketFragment;
import com.baishan.nearshop.utils.ConstantValue;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class BaseLink implements Serializable {


    public int LinkType;
    public String LinkValue;
    public String Title;


    public void click(Context context) {
        switch (LinkType) {
            case 1://网页
                Intent it1 = new Intent(context, WebActivity.class);
                it1.putExtra(ConstantValue.URL, LinkValue);
                it1.putExtra(ConstantValue.TITLE, Title);
                context.startActivity(it1);
                break;
            case 2://商品分类
                MainActivity.getInstance().showFragment(1);
                SupermarketFragment fragment = (SupermarketFragment) FragmentController.getInstance().getFragment(1);
                fragment.show(Integer.parseInt(LinkValue));
                break;
            case 3://商品
                Intent it2 = new Intent(context, GoodsDetailActivity.class);
                it2.putExtra(ConstantValue.DATA, Integer.parseInt(LinkValue));
                context.startActivity(it2);
                break;
            case 4://服务分类
                MainActivity.getInstance().showFragment(2);
                EasyOrderFragment ef = (EasyOrderFragment) FragmentController.getInstance().getFragment(2);
                ef.showTypeList((GridType) this);
                break;
            case 5://服务
                Intent it3 = new Intent(context, ServiceDetailActivity.class);
                it3.putExtra(ConstantValue.DATA, Integer.parseInt(LinkValue));
                context.startActivity(it3);
                break;
            case 6://商品特殊分类
                Intent it4 = new Intent(context, GoodsListActivity.class);
                it4.putExtra(ConstantValue.TITLE, Title);
                it4.putExtra(ConstantValue.CATEGORY_TYPE, LinkValue);
                context.startActivity(it4);
                break;

        }
    }
}
