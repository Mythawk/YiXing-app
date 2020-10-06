package com.mythawk.yixing;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mythawk.yixing.utils.LitepalCategory;

import org.litepal.LitePal;

public class YiXingApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Fresco.initialize(this);
        initCategory();
        initIsLogin();
    }

    private void initIsLogin() {
        SharedPreferences pref = getSharedPreferences("initData",MODE_PRIVATE);
        int isLogin = pref.getInt("isLogin",0);
        if (isLogin == 0 ){
            SharedPreferences.Editor editor = getSharedPreferences("isFirst",MODE_PRIVATE).edit();
            editor.putInt("isLogin",2);
            editor.apply();
        }
    }

    private void initCategory() {
        SharedPreferences pref = getSharedPreferences("initData",MODE_PRIVATE);
        int isFirst = pref.getInt("isFirst",1);
        if (isFirst == 1 ){

            SharedPreferences.Editor editor = getSharedPreferences("isFirst",MODE_PRIVATE).edit();
            editor.putInt("isFirst",2);
            editor.apply();
            LitepalCategory category = new LitepalCategory();
            category.cleandata();
            category.init();
        }

    }
}
