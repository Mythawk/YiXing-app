package com.mythawk.yixing.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.mythawk.yixing.Gson.RankBody;
import com.mythawk.yixing.R;
import com.mythawk.yixing.ShoesActivity;
import com.mythawk.yixing.adapter.RankAdapter;
import com.mythawk.yixing.bean.Rank;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.CnToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class RankFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout mReFreshLayout;
    private CnToolbar cnToolbar;
    private ProgressDialog dialog;

    private List<Rank> rankList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank,container,false);

        cnToolbar = view.findViewById(R.id.toolbar);
        mReFreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        initToolbar();
        requestRankData();


        mReFreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                requestRankData();
                mReFreshLayout.finishRefresh();
            }
        });


        return view;
    }

    private void requestRankData() {
        dialog = ProgressDialog.show(getContext(),null,"获取数据中...",false,true);
        String url = BASEURL + "scout/getScoutRank";
        OkHttpUtil.sendRankRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"出现错误!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final RankBody rankBody = Utility.handleRankResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(rankBody.status)&&rankBody!=null){
                            rankList = rankBody.rankData;
                            RankAdapter rankAdapter = new RankAdapter(rankList);
                            mRecyclerView.setAdapter(rankAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            rankAdapter.setOnClickListener(new RankAdapter.OnClickListener() {
                                @Override
                                public void onClick(View itemView, int position) {
                                    Intent intent = new Intent(getContext(),ShoesActivity.class);
                                    intent.putExtra("name",rankList.get(position).getRank_name());
                                    startActivity(intent);
                                }
                            });
                        }else {
                            Toast.makeText(getActivity(),"出现错误了，请重试！",Toast.LENGTH_SHORT).show();
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
        cnToolbar.setTitle(R.string.rank);
    }


}
