package com.mythawk.yixing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.wrappers.UMSSDKWrapper;
import com.mythawk.yixing.Gson.BaseBody;
import com.mythawk.yixing.Gson.OrderBody;
import com.mythawk.yixing.adapter.MyOrderAdapter;
import com.mythawk.yixing.bean.OrderCount;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private CnToolbar cnToolbar;
    private TextView textUnPay;
    private TextView textPay;
    private TextView textResult;
    private TextView textGet;
    private TextView textAll;
    private TextView textNoData;
    private RecyclerView recyclerView;

    private int aciton = 1;
    private String stauts;
    public static final int ACTIONNOPAY = 1;
    public static final int ACTIONPAY = 2;
    public static final int ACTIONGet = 3;
    public static final int ACTIONRESULT = 4;
    public static final int ACTIONALL = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        cnToolbar = findViewById(R.id.toolbar);
        textUnPay = findViewById(R.id.txt_no_pay);
        textPay = findViewById(R.id.txt_pay);
        textResult = findViewById(R.id.txt_result);
        textGet = findViewById(R.id.txt_get);
        recyclerView = findViewById(R.id.recycler_view);
        textAll = findViewById(R.id.txt_all);
        textNoData = findViewById(R.id.txt_no_data);
        textNoData.setVisibility(View.GONE);

        textUnPay.setOnClickListener(this);
        textPay.setOnClickListener(this);
        textGet.setOnClickListener(this);
        textResult.setOnClickListener(this);
        textAll.setOnClickListener(this);

        intToolbar();
        getData("ALL",5);
    }

    private void intToolbar() {
        cnToolbar.hideSearchView();
        cnToolbar.showTitleView();
        cnToolbar.setTitle(R.string.myOrder_tittle);
        cnToolbar.setNavigationIcon(R.mipmap.icon_back_button);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_no_pay:
                aciton=ACTIONNOPAY;
                stauts = "未支付";
                getData(stauts,aciton);
                break;
            case R.id.txt_pay:
                aciton=ACTIONPAY;
                stauts = "待发货";
                getData(stauts,aciton);
                break;
            case R.id.txt_get:
                aciton=ACTIONGet;
                stauts = "待收货";
                getData(stauts,aciton);
                break;
            case R.id.txt_result:
                aciton=ACTIONRESULT;
                stauts = "已完成";
                getData(stauts,aciton);
                break;
            case R.id.txt_all:
                aciton=ACTIONALL;
                stauts = "ALL";
                getData(stauts,aciton);
                break;
        }
    }

    private void getData(String status , final int action){
        recyclerView.setVisibility(View.VISIBLE);
        String url =BASEURL +"order/selectOrder";
        String phone = LitePal.findFirst(Users.class).getNumb();
        final Dialog dialog = ProgressDialog.show(MyOrderActivity.this,null,"正在查询...",false,true);
        OkHttpUtil.sendSelectOrderRequest(url, phone,status, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyOrderActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final OrderBody orderBody = Utility.handleSelectOrderResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (orderBody!=null && "ok".equals(orderBody.status)){
                            List<OrderCount> orderCountList = orderBody.data;
                            MyOrderAdapter myOrderAdapter =new MyOrderAdapter(orderCountList,action,MyOrderActivity.this);
                            recyclerView.setAdapter(myOrderAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void sendAction(final int aciton2,String orderId){
        String url =BASEURL +"order/editOrder";
        final Dialog dialog2 = ProgressDialog.show(MyOrderActivity.this,null,"加载中...",false,true);
        OkHttpUtil.sendEditOrderRequest(url,orderId,aciton2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyOrderActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
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
                        if ("ok".equals(baseBody.status)){
                            dialog2.dismiss();
                            switch (aciton2){
                                case 2:
                                    Toast.makeText(MyOrderActivity.this,"已取消订单",Toast.LENGTH_SHORT).show();
                                    getData(stauts,aciton);
                                    break;
                                case 3:
                                    Toast.makeText(MyOrderActivity.this,"订单已完成",Toast.LENGTH_SHORT).show();
                                    getData(stauts,aciton);
                                    break;
                                case 4:
                                    Toast.makeText(MyOrderActivity.this,"已发出退货申请",Toast.LENGTH_SHORT).show();
                                    getData(stauts,aciton);
                                    break;
                            }
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            textNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 3:
                if (resultCode == RESULT_OK){
                    getData("未支付",ACTIONNOPAY);
                }
                break;
        }
    }


}
