package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.mythawk.yixing.bean.Tab;
import com.mythawk.yixing.fragment.CartFragment;
import com.mythawk.yixing.fragment.CategoryFragment;
import com.mythawk.yixing.fragment.HomeFragment;
import com.mythawk.yixing.fragment.MineFragment;
import com.mythawk.yixing.fragment.RankFragment;
import com.mythawk.yixing.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private FragmentTabHost tabHost;
    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeTab(intent);
    }

    public void changeTab(Intent intent){
        Boolean isChange = intent.getBooleanExtra("toCart",false);
        while (isChange){
            Log.d("test:", "onCreate: "+ intent.getBooleanExtra("toCart",false) );
            tabHost.setCurrentTab(3);
            tabHost.onTabChanged("购物车");
            isChange = false;
        }
    }


    private void initTab() {//初始化tabHost，添加每个tab,并绑定样式

        Tab tab_home = new Tab(R.string.home,R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_category = new Tab(R.string.category,R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_rank = new Tab(R.string.rank,R.drawable.selector_icon_rank, RankFragment.class);
        Tab tab_Cart = new Tab(R.string.cart,R.drawable.selector_icon_cart, CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine,R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(tab_home);
        mTabs.add(tab_category);
        mTabs.add(tab_rank);
        mTabs.add(tab_Cart);
        mTabs.add(tab_mine);

        inflater = LayoutInflater.from(this);
        tabHost = (FragmentTabHost)this.findViewById(android.R.id.tabhost) ;

        tabHost.setup(this,getSupportFragmentManager(),R.id.realTabContent);


        for (Tab tab : mTabs){

            TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(tab.getTxt()));
            tabSpec.setIndicator(buildIndicator(tab));
            tabHost.addTab(tabSpec,tab.getFragment(),null);
        }

        tabHost.setCurrentTab(0);//默认选择主业

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(R.string.cart)){
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
                    CartFragment cartFragmnet= (CartFragment)fragment;
                    cartFragmnet.refData();
                }
            }
        });

    }
    private View buildIndicator(Tab tab){//对菜单的每一个tab进行初始化

        View view = inflater.inflate(R.layout.item_tab,null);
        ImageView img = (ImageView)view.findViewById(R.id.tab_Img);
        TextView txt = (TextView)view.findViewById(R.id.tab_txt);

        img.setBackgroundResource(tab.getImg());
        txt.setText(tab.getTxt());
        return view;
    }

    public static class SoftKeyboardUtils {

        /**
         * 关闭软件盘
         */
        public static void closeInoutDecorView(Activity activity) {
            View view = activity.getWindow().peekDecorView();
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}
