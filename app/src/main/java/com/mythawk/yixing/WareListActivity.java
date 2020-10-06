package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.google.android.material.tabs.TabLayout;
import com.mythawk.yixing.Gson.SneakerBody;
import com.mythawk.yixing.adapter.WareListAdapter;
import com.mythawk.yixing.bean.Sneaker;
import com.mythawk.yixing.bean.SneakerPost;
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

public class WareListActivity extends AppCompatActivity implements View.OnClickListener{

    private CnToolbar toolbar;
    private RecyclerView mRecyclerView;
    private TextView mTxtSummary;
    private ProgressDialog dialog;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GIRD=2;
    public static final String TAG = "WareListActivity:";

    private SneakerPost sneakerPost ;
    private List<Sneaker>sneakerList = new ArrayList<>() ;
    private WareListAdapter mWareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);

        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mTxtSummary = findViewById(R.id.txt_summary);


        initToolbar();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        String content = intent.getStringExtra("name");
        int position = intent.getIntExtra("category",0) ;
        Log.d(TAG, "getData: "+content+position);

        sneakerPost = new SneakerPost();
        switch (position){
            case 0:
                if (content.equals("All Shoes")){
                    sneakerPost.setMethod("all");
                    sneakerPost.setContent("all");
                }else {
                    sneakerPost.setMethod("band");
                    sneakerPost.setContent(content);
                }
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if (content.equals("More")){
                    sneakerPost.setMethod("category");
                    sneakerPost.setContent(""+position);
                }else {
                    sneakerPost.setMethod("name");
                    sneakerPost.setContent(content);
                }
                break;
        }
        requestData();
    }

    private void requestData() {
        dialog = ProgressDialog.show(this,null,"正在加载...",false,true);
        String url = BASEURL+"sneaker/redata";
        OkHttpUtil.sendSneakerRequest(url, sneakerPost, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WareListActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final SneakerBody sneakerBody = Utility.handleSneakerResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(sneakerBody.status)&&sneakerBody!=null){
                            sneakerList = sneakerBody.sneakerList;
                            mTxtSummary.setText("共有"+sneakerList.size()+"商品");
                            initWareAdapter();
                        }else {
                            Toast.makeText(WareListActivity.this,"未能找到物品！",Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void initWareAdapter(){
        if (mWareAdapter == null){
            mWareAdapter = new WareListAdapter(sneakerList,R.layout.template_warelist_hot);
            mRecyclerView.setAdapter(mWareAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }else {
            mWareAdapter.refreshData(sneakerList);
        }
        mWareAdapter.setOnClickListener(new WareListAdapter.OnClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                Intent intent = new Intent(WareListActivity.this,ShoesActivity.class);
                intent.putExtra("name",sneakerList.get(position).getSneaker_name());
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightButtonIcon(R.mipmap.icon_warelist_grid);
        toolbar.getRightButton().setTag(ACTION_LIST);
        toolbar.setRightButtonOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        int action = (int)v.getTag();

        if (ACTION_LIST == action){
            toolbar.setRightButtonIcon(R.mipmap.icon_warelist_list);
            toolbar.getRightButton().setTag(ACTION_GIRD);
            //设置adapter
            mWareAdapter = new WareListAdapter(sneakerList,R.layout.template_warelist_grid);
            mRecyclerView.setAdapter(mWareAdapter);
            mWareAdapter.setOnClickListener(new WareListAdapter.OnClickListener() {
                @Override
                public void onClick(View itemView, int position) {
                    Intent intent = new Intent(WareListActivity.this,ShoesActivity.class);
                    intent.putExtra("name",sneakerList.get(position).getSneaker_name());
                    startActivity(intent);
                }
            });
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        }else if (ACTION_GIRD == action){
            toolbar.setRightButtonIcon(R.mipmap.icon_warelist_grid);
            toolbar.getRightButton().setTag(ACTION_LIST);
            //设置adapter
            mWareAdapter = new WareListAdapter(sneakerList,R.layout.template_warelist_hot);
            mRecyclerView.setAdapter(mWareAdapter);
            mWareAdapter.setOnClickListener(new WareListAdapter.OnClickListener() {
                @Override
                public void onClick(View itemView, int position) {
                    Intent intent = new Intent(WareListActivity.this,ShoesActivity.class);
                    intent.putExtra("name",sneakerList.get(position).getSneaker_name());
                    startActivity(intent);
                }
            });
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        }

    }
}
