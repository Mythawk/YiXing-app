package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mythawk.yixing.Gson.ShareIdBody;
import com.mythawk.yixing.bean.Share;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class MainShareActivity extends AppCompatActivity {

    private CnToolbar cnToolbar;
    private CircleImageView imgHead;
    private TextView txtHead;
    private TextClock txtClock;
    private TextView shareTittle;
    private ImageView shareImg;
    private TextView shock;
    private TextView parcel;
    private TextView support;
    private TextView grip;
    private TextView durable;
    private TextView average;
    private TextView shareMain;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_show);

        cnToolbar = findViewById(R.id.toolbar);
        imgHead = findViewById(R.id.img_head);
        txtHead = findViewById(R.id.txt_head);
        txtClock = findViewById(R.id.txt_clock);
        shareTittle = findViewById(R.id.share_title);
        shareImg = findViewById(R.id.share_img);
        shock = findViewById(R.id.shock);
        parcel = findViewById(R.id.parcel);
        support = findViewById(R.id.support);
        grip = findViewById(R.id.grip);
        durable = findViewById(R.id.durable);
        average = findViewById(R.id.average);
        shareMain = findViewById(R.id.share_main);


        Intent intent = getIntent();
        id = intent.getIntExtra("data",0);

        if (id != 0){
            requestData();
        }else {
            Toast.makeText(MainShareActivity.this,"出现了未知的错误！",Toast.LENGTH_SHORT).show();
        }

        initToolbar();
    }

    private void initToolbar() {
        cnToolbar.setNavigationIcon(R.mipmap.icon_back_button);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cnToolbar.setRightButtonIcon(R.mipmap.icon_share_txt);
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users users = LitePal.findFirst(Users.class);
                if (users != null){
                    Intent intent = new Intent(MainShareActivity.this, CommentActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainShareActivity.this,"你需要先登录才能留言哦!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestData(){
        String url = BASEURL+"share/redataById";
        final Dialog dialog = ProgressDialog.show(MainShareActivity.this,null,"正在加载...",false,true);
        OkHttpUtil.sendShareRequest(url, id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( MainShareActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final ShareIdBody ShareIdBody = Utility.handleShareByIdResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if ("ok".equals(ShareIdBody.status)&&ShareIdBody.redata!=null){
                            Share share = ShareIdBody.redata;
                            Glide.with(MainShareActivity.this).load(share.getUser_image()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgHead);
                            txtHead.setText(share.getUser_name());
                            txtClock.setText(share.getDate());
                            shareTittle.setText(share.getShare_topic());
                            Glide.with(MainShareActivity.this).load(share.getImg()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(shareImg);
                            shock.setText(String.valueOf(share.getShock()));
                            parcel.setText(String.valueOf(share.getParcel()));
                            support.setText(String.valueOf(share.getSupport()));
                            grip.setText(String.valueOf(share.getGrip()));
                            durable.setText(String.valueOf(share.getDurable()));
                            average.setText(String.valueOf(share.getScout()));
                            shareMain.setText(share.getShare_txt());



                        }else {
                            Toast.makeText(MainShareActivity.this,"出现错误请重试！",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

}
