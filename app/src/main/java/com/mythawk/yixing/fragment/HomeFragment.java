package com.mythawk.yixing.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.mythawk.yixing.AddShareActivity;
import com.mythawk.yixing.Gson.ShareBody;
import com.mythawk.yixing.MainShareActivity;
import com.mythawk.yixing.R;
import com.mythawk.yixing.adapter.HomeAdapter;
import com.mythawk.yixing.bean.Share;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class HomeFragment extends Fragment {

    private CnToolbar cnToolbar;
    private RecyclerView recyclerView;
    private MaterialRefreshLayout mRefreshLayout;

    private List<Share> shareList = new ArrayList<>();
    private List<Share> shareMainList = new ArrayList<>();
    private int dataSize = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        cnToolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.home_recycler);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);


        requestData();//初始化数据
        initToolbar();
        initRecyclerView();

        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                requestData();
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (dataSize * 10 < shareMainList.size()) {
                    dataSize++;
                    if (dataSize*10 < shareMainList.size()) {
                        shareList = shareMainList.subList(0, dataSize * 10);
                    }else {
                        shareList = shareMainList;
                    }
                    initRecyclerView();
                    recyclerView.scrollToPosition((dataSize - 1) * 10 - 1 );
                } else{
                    Toast.makeText(getContext(), "已经到底了！", Toast.LENGTH_SHORT).show();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefreshLoadMore();
                    }
                },1000);
            }
        });

        return view;
    }



    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        HomeAdapter adapter = new HomeAdapter(shareList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnClickListener(new HomeAdapter.OnClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                Intent intent =new Intent(getContext(), MainShareActivity.class);
                intent.putExtra("data",shareList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void requestData() {
        dataSize = 1;
        String url = BASEURL + "share/redata";
        final Dialog dialog = ProgressDialog.show(getContext(),null,"正在加载...",false,true);
        OkHttpUtil.sendShareRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( getContext(),"出现错误!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final ShareBody shareBody = Utility.handleShareResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(shareBody.status)&&shareBody.redata!=null){

                            shareMainList = shareBody.redata;
                            if(shareMainList.size()>=10) {
                                shareList = shareMainList.subList(0, 10);
                            }else {
                                shareList = shareMainList;
                            }
                            initRecyclerView();
                        }else {
                            Toast.makeText(getContext(),"出现错误请重试！",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });


    }


    private void initToolbar() {
        cnToolbar.showTitleView();
        cnToolbar.hideSearchView();
        cnToolbar.setTitle(R.string.app_name);

        cnToolbar.setRightButtonIcon(R.mipmap.icon_add);
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users users = LitePal.findFirst(Users.class);
                if (users == null || users.getName()==null){
                    Toast.makeText(getContext(),"你需要先登录才能发表分享!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getContext(), AddShareActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (dataSize!=1) {
            requestData();
        }
    }
}
