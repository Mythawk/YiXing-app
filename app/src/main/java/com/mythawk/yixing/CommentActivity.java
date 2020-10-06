package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mythawk.yixing.Gson.ComBody;
import com.mythawk.yixing.adapter.ComAdapter;
import com.mythawk.yixing.bean.ComData;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText comText;
    private Button comBtn;
    private int shareId;

    private List<ComData> comDataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commont);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        comText = findViewById(R.id.com_edit);
        comBtn = findViewById(R.id.com_btn);

        shareId =  getIntent().getIntExtra("id",0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        comBtn.setOnClickListener(this);

        if (shareId!=0){
            requestData();
        }else {
            Toast.makeText(this,"出现了不可知的错误！请重试",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void requestData() {
        String url = BASEURL +"com/getComById";
        final Dialog dialog = ProgressDialog.show(this,null,"正在加载...",false,true);
        OkHttpUtil.sendShareRequest(url, shareId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommentActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final ComBody comBody = Utility.handleComRequestResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(comBody.status)&&comBody.comData!=null){
                            comDataList = comBody.comData;
                            initRecyclerView();
                        }else {
                            Toast.makeText(CommentActivity.this,"暂时还没有留言哦!",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ComAdapter adapter = new ComAdapter(comDataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.com_btn:
                Users users = LitePal.findFirst(Users.class);
                ComData comData = new ComData();
                comData.setShare_id(shareId);
                comData.setCom_num(users.getNumb());
                comData.setCom_text(comText.getText().toString());
                if (comDataList.size() != 0) {
                    comData.setCom_floor(comDataList.size() + 1);
                }else {
                    comData.setCom_floor(1);
                }
                comText.setText("");
                String url = BASEURL +"com/insertCom";
                final Dialog dialog = ProgressDialog.show(this,null,"正在上传...",false,true);
                OkHttpUtil.sendComRequest(url, comData, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommentActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        final ComBody comBody = Utility.handleComRequestResponse(responseText);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("ok".equals(comBody.status)  && comBody.comData != null) {
                                    comDataList = comBody.comData;
                                    initRecyclerView();
                                } else {
                                    Toast.makeText(CommentActivity.this, "出现错误请重试！", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                });

                break;
        }
    }
}
