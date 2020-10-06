package com.mythawk.yixing.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cjj.MaterialRefreshLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mythawk.yixing.MainActivity;
import com.mythawk.yixing.R;
import com.mythawk.yixing.WareListActivity;
import com.mythawk.yixing.adapter.CategoryMainAdapter;
import com.mythawk.yixing.adapter.CategoryMenuAdapter;
import com.mythawk.yixing.bean.Banner;
import com.mythawk.yixing.litepal.CategoryItem;
import com.mythawk.yixing.bean.CategoryMainItem;
import com.mythawk.yixing.bean.CategoryMenuItem;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private SliderLayout sliderLayout;
    private MaterialRefreshLayout mRefreshLayout;
    private CnToolbar cnToolbar;

    private CategoryMenuAdapter categoryMenuAdapter;
    private final static String TAG = "CategoryFragment";

    List<Banner> banners = new ArrayList<>();
    List<CategoryMainItem>categoryMainItemList = new ArrayList<>();
    List<CategoryMenuItem>categoryMenuItemList = new ArrayList<>() ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);

        sliderLayout = view.findViewById(R.id.slider);
        recyclerView = view.findViewById(R.id.category_recycler);
        recyclerView2 = view.findViewById(R.id.category_wares);
        cnToolbar = view.findViewById(R.id.toolbar);

        ShowSliderLayout();
        initData();
        initToolbar();
        ShowRecyclerMain();
        ShowRecyclerMenu(0);
        return view;

    }



    private void ShowRecyclerMain() {

        CategoryMainAdapter adapter1 = new CategoryMainAdapter(categoryMainItemList);
        adapter1.setOnClickListener(new CategoryMainAdapter.OnClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                ShowRecyclerMenu(position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

    }

    private void ShowRecyclerMenu(int position) {
        List<CategoryItem> categoryItemList = LitePal.where("cate = ?",""+position).find(CategoryItem.class);
        categoryMenuItemList = new ArrayList<>();
        for (CategoryItem categoryItem :categoryItemList){
            CategoryMenuItem categoryMenuItem = new CategoryMenuItem();
            categoryMenuItem.setImgId(categoryItem.getImgId());
            categoryMenuItem.setName(categoryItem.getName());
            Log.d("test:", "ShowRecyclerMenu: "+categoryMenuItem.getName());
            categoryMenuItemList.add(categoryMenuItem);
        }


        if (categoryMenuAdapter == null){
            final List<CategoryItem> categoryItemList2 = categoryItemList;
            final int position2 = position;
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
            recyclerView2.setLayoutManager(layoutManager);
            categoryMenuAdapter = new CategoryMenuAdapter(categoryMenuItemList);
            recyclerView2.setAdapter(categoryMenuAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            categoryMenuAdapter.setOnClickListener(new CategoryMenuAdapter.OnClickListener() {
                @Override
                public void onClick(View itemView, int positions) {
                    Intent intent = new Intent(getContext(), WareListActivity.class);
                    String content = categoryItemList2.get(positions).getName();
                    intent.putExtra("name",content);
                    intent.putExtra("category",position2);
                    startActivity(intent);
                }
            });
        }else {
            categoryMenuAdapter.clear();
            categoryMenuAdapter.addData(categoryMenuItemList);
            final List<CategoryItem> categoryItemList2 = categoryItemList;
            final int position2 = position;
            categoryMenuAdapter.setOnClickListener(new CategoryMenuAdapter.OnClickListener() {
                @Override
                public void onClick(View itemView, int positions) {
                    Intent intent = new Intent(getContext(), WareListActivity.class);
                    String content = categoryItemList2.get(positions).getName();
                    intent.putExtra("name",content);
                    intent.putExtra("category",position2);
                    startActivity(intent);
                }
            });

        }
    }


    private void initData() {

        CategoryMainItem categoryMainItem = new CategoryMainItem();
        categoryMainItem.setName("热门品牌");
        categoryMainItemList.add(categoryMainItem);
        categoryMainItem = new CategoryMainItem();
        categoryMainItem.setName("篮球鞋");
        categoryMainItemList.add(categoryMainItem);
        categoryMainItem = new CategoryMainItem();
        categoryMainItem.setName("跑鞋");
        categoryMainItemList.add(categoryMainItem);
        categoryMainItem = new CategoryMainItem();
        categoryMainItem.setName("休闲鞋");
        categoryMainItemList.add(categoryMainItem);
        categoryMainItem = new CategoryMainItem();
        categoryMainItem.setName("其他鞋类");
        categoryMainItemList.add(categoryMainItem);

    }

    private void ShowSliderLayout() {

        InitSliderData();

        if (banners != null){

            for (Banner banner : banners){
                TextSliderView sliderView = new TextSliderView(this.getActivity());
                sliderView.image(banner.getImgId());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(sliderView);
            }

        }
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setDuration(3000);

    }

    private void InitSliderData() {

        Banner banner = new Banner("安踏",R.mipmap.img_anta);
        banners.add(banner);
        banner = new Banner("李宁",R.mipmap.img_lining);
        banners.add(banner);
        banner = new Banner("361°",R.mipmap.img_361);
        banners.add(banner);
        banner = new Banner("鸿星尔克",R.mipmap.img_hxek);
        banners.add(banner);
        banner = new Banner("匹克",R.mipmap.img_pktj);
        banners.add(banner);

    }

    private void initToolbar() {
        cnToolbar.hideTitleView();
        cnToolbar.showSearchView();
        final EditText editText = cnToolbar.getSearchView();
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = editText.getText().toString();
                if (content!=null){
                    MainActivity.SoftKeyboardUtils.closeInoutDecorView(getActivity());
                    Toast.makeText(getContext(),"start search: "+content,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), WareListActivity.class);
                    intent.putExtra("name",content);
                    intent.putExtra("category",5);
                    startActivity(intent);
                    editText.setText(null);
                }
                return false;
            }
        });
        cnToolbar.setRightButtonText("取消");
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText.setCursorVisible(false);
                MainActivity.SoftKeyboardUtils.closeInoutDecorView(getActivity());
            }
        });
    }

}
