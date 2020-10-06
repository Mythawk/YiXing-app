package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mythawk.yixing.Gson.BaseBody;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class PayActivity extends AppCompatActivity {

    private TextView payWay;
    private TextView payData;
    private Button btnBuy;
    private Button btnBack;
    private MyOrderActivity myOrderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        payWay = findViewById(R.id.pay_way);
        payData = findViewById(R.id.pay_data);
        btnBuy = findViewById(R.id.btn_buy);
        btnBack = findViewById(R.id.btn_back);


        Intent intent = getIntent();
        String way = intent.getStringExtra("way");
        double sum = intent.getDoubleExtra("sum",0.0);
        final String id = intent.getStringExtra("id");


        payWay.setText(way);
        payData.setText(String.valueOf(sum));
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction(1,id);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

    }
    private void sendAction(final int aciton2,String orderId){
        String url =BASEURL +"order/editOrder";
        final Dialog dialog2 = ProgressDialog.show(PayActivity.this,null,"确认中...",false,true);
        OkHttpUtil.sendEditOrderRequest(url, orderId, aciton2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog2.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final BaseBody baseBody = Utility.handleEditOrderResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(baseBody.status)) {
                            dialog2.dismiss();
                            final Dialog dialog3 = ProgressDialog.show(PayActivity.this,null,"支付成功!",false,true);
                            Handler handler =new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog3.dismiss();
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            },2000);
                        }else {
                            final Dialog dialog4 = ProgressDialog.show(PayActivity.this,null,"支付失败!",false,true);
                            Handler handler =new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog4.dismiss();
                                    Intent intent = new Intent();
                                    setResult(RESULT_CANCELED,intent);
                                    finish();
                                }
                            },2000);
                        }
                    }
                });
            }
        });
    }
}
