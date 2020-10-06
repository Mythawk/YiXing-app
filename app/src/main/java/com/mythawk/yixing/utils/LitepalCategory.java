package com.mythawk.yixing.utils;

import com.mythawk.yixing.R;
import com.mythawk.yixing.litepal.CategoryItem;

import org.litepal.LitePal;

public class LitepalCategory {

    public void init(){
        CategoryItem categoryItem = new CategoryItem();
        categoryItem.setCate(0);
        categoryItem.setName("李宁");
        categoryItem.setImgId(R.mipmap.icon_category_lining);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(0);
        categoryItem.setName("安踏");
        categoryItem.setImgId(R.mipmap.icon_category_anta);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(0);
        categoryItem.setName("匹克");
        categoryItem.setImgId(R.mipmap.icon_category_pike);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(0);
        categoryItem.setName("鸿星尔克");
        categoryItem.setImgId(R.mipmap.icon_category_hxek);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(0);
        categoryItem.setName("361°");
        categoryItem.setImgId(R.mipmap.icon_category_361);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(0);
        categoryItem.setName("All Shoes");
        categoryItem.setImgId(R.mipmap.icon_category_more);
        categoryItem.save();

        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("音速");
        categoryItem.setImgId(R.mipmap.icon_category_yinsu);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("韦德之道");
        categoryItem.setImgId(R.mipmap.icon_category_wow);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("驭帅");
        categoryItem.setImgId(R.mipmap.icon_category_yushuai);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("KT");
        categoryItem.setImgId(R.mipmap.icon_category_kt);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("闪现");
        categoryItem.setImgId(R.mipmap.icon_category_shangxian);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("戈登");
        categoryItem.setImgId(R.mipmap.icon_category_big3);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(1);
        categoryItem.setName("More");
        categoryItem.setImgId(R.mipmap.icon_category_more);
        categoryItem.save();


        categoryItem = new CategoryItem();
        categoryItem.setCate(2);
        categoryItem.setName("超轻");
        categoryItem.setImgId(R.mipmap.icon_category_chaoqin);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(2);
        categoryItem.setName("奇弹");
        categoryItem.setImgId(R.mipmap.icon_category_qitan);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(2);
        categoryItem.setName("氢");
        categoryItem.setImgId(R.mipmap.icon_category_qin);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(2);
        categoryItem.setName("More");
        categoryItem.setImgId(R.mipmap.icon_category_more);
        categoryItem.save();

        categoryItem = new CategoryItem();
        categoryItem.setCate(3);
        categoryItem.setName("态极");
        categoryItem.setImgId(R.mipmap.icon_category_taiji);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(3);
        categoryItem.setName("悟道");
        categoryItem.setImgId(R.mipmap.icon_category_wudao);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(3);
        categoryItem.setName("More");
        categoryItem.setImgId(R.mipmap.icon_category_more);
        categoryItem.save();

        categoryItem = new CategoryItem();
        categoryItem.setCate(4);
        categoryItem.setName("老爹鞋");
        categoryItem.setImgId(R.mipmap.icon_category_hudie);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(4);
        categoryItem.setName("健身鞋");
        categoryItem.setImgId(R.mipmap.icon_category_wujiex);
        categoryItem.save();
        categoryItem = new CategoryItem();
        categoryItem.setCate(4);
        categoryItem.setName("More");
        categoryItem.setImgId(R.mipmap.icon_category_more);
        categoryItem.save();
    }

    public void cleandata(){
        LitePal.deleteAll(CategoryItem.class,"cate >= ?","0");
    }



}
