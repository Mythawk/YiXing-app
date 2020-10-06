package com.mythawk.yixing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mythawk.yixing.Gson.BaseBody;
import com.mythawk.yixing.Gson.UsersBody;
import com.mythawk.yixing.adapter.OrderAdapter;
import com.mythawk.yixing.bean.Order;
import com.mythawk.yixing.litepal.Address;
import com.mythawk.yixing.litepal.Cart;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private CnToolbar cnToolbar;
    private RecyclerView mRecyclerView;
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtAddress;
    private LinearLayout orderAddress;
    private TextView txtTotal;
    private RadioButton mRbAlipay;
    private RadioButton mRbWechat;
    private Button btnCreateOrder;
    private EditText txtNote;

    private int checkedItem = 0;
    private double sum = 0.00;
    private String address;
    private String detail;

    private List<Cart> cartList = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        cnToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtAddress = findViewById(R.id.txt_address);
        orderAddress = findViewById(R.id.order_address);
        txtTotal = findViewById(R.id.txt_total);
        mRbAlipay = findViewById(R.id.rb_alipay);
        mRbWechat = findViewById(R.id.rb_webchat);
        btnCreateOrder = findViewById(R.id.btn_createOrder);
        txtNote = findViewById(R.id.edit_note);

        btnCreateOrder.setOnClickListener(this);

        showData();
        init();
        chooseAddress();
    }

    private void chooseAddress() {

        txtName.setText("请选择收货地址");
        orderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Address> addressList = LitePal.findAll(Address.class);
                addresses = addressList;
                String[] cities = new String[addressList.size()];
                int i = 0;
                for (Address address:addressList){
                    String add = address.getName()+" "+address.getName()+" "+address.getAddress();
                    cities[i] = add;
                    i++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                builder.setTitle("选择收货地址");
                builder.setSingleChoiceItems(cities, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem = which;
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Address address2 = addressList.get(checkedItem);
                        txtName.setText(address2.getName());
                        txtPhone.setText(address2.getPhone());
                        txtAddress.setText(address2.getAddress()+" "+address2.getDetail());
                        address = address2.getAddress();
                        detail = address2.getDetail();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();  //创建AlertDialog对象
                dialog.show();                           //显示对话框


            }
        });

    }


    private void showData(){
        ArrayList<String> dataList = (ArrayList<String>)getIntent().getStringArrayListExtra("data");
        for (String id : dataList){
            long cartId =  (long)Integer.parseInt(id);
            cartList.add(LitePal.find(Cart.class,cartId));
        }

        Log.d("test:", "onCreate: "+cartList);
        for (Cart cart :cartList){
            sum += cart.getCart_number() * cart.getCart_price();
        }
        txtTotal.setText("实付： ￥"+sum);


        OrderAdapter orderAdapter =new OrderAdapter(cartList);
        mRecyclerView.setAdapter(orderAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void init(){

        cnToolbar.setNavigationIcon(R.mipmap.icon_back_button);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRbAlipay.setChecked(true);
        mRbWechat.setChecked(false);

        mRbAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbAlipay.setChecked(true);
                mRbWechat.setChecked(false);
            }
        });
        mRbWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbAlipay.setChecked(false);
                mRbWechat.setChecked(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_createOrder:
                final List<Order> orderList =new ArrayList<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
                String dataTime = dateFormat.format(date);
                Users users = LitePal.findFirst(Users.class);
                String s= users.getNumb();
                String id = dataTime+ s.substring(s.length()-4,s.length());
                String name = txtName.getText().toString();
                if (name!=null && address !=null) {
                    for (Cart cart : cartList) {
                        Order order = new Order();
                        order.setOrder_id(id);
                        order.setUser_number(users.getNumb());
                        order.setSneaker_name(cart.getCart_band() + cart.getCart_shoes());
                        order.setSneaker_image(cart.getCart_image());
                        order.setSneaker_number(cart.getCart_number());
                        order.setSneaker_price(cart.getCart_price());
                        order.setSneaker_size(cart.getCart_size());
                        order.setAddress_name(txtName.getText().toString());
                        order.setAddress_phone(txtPhone.getText().toString());
                        order.setAddress_address(address);
                        order.setAddress_detail(detail);
                        order.setOrder_note(txtNote.getText().toString());
                        order.setOrder_status("未支付");
                        orderList.add(order);
                    }

                    String url = BASEURL + "order/addOrder";
                    final Dialog dialog = ProgressDialog.show(OrderActivity.this, null, "正在生成订单...", false, true);
                    OkHttpUtil.sendAddOrderRequest(url, orderList, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OrderActivity.this, "生成订单失败!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            final BaseBody baseBody = Utility.handleAddOrderResponse(responseText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("ok".equals(baseBody.status)) {
                                        Toast.makeText(OrderActivity.this, "生成订单成功!", Toast.LENGTH_SHORT).show();
                                        for (Cart cart : cartList) {
                                            LitePal.delete(Cart.class, cart.getId());
                                        }
                                        Intent intent = new Intent(OrderActivity.this, PayActivity.class);
                                        intent.putExtra("sum", sum);
                                        if (mRbAlipay.isChecked()) {
                                            intent.putExtra("way", "支付宝");
                                            intent.putExtra("id", orderList.get(0).getOrder_id());
                                        } else {
                                            intent.putExtra("way", "微信支付");
                                        }
                                        startActivityForResult(intent, 2);
                                    } else {
                                        Toast.makeText(OrderActivity.this, "生成订单失败", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            });

                        }
                    });
                }else {
                    Toast.makeText(OrderActivity.this,"您还没有选择收货地址！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if (resultCode == RESULT_OK){
                    final Dialog dialog2 = ProgressDialog.show(OrderActivity.this,null,"支付成功!",false,true);
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog2.dismiss();
                            finish();
                        }
                    },2000);
                }else {
                    final Dialog dialog3 = ProgressDialog.show(OrderActivity.this,null,"支付失败!请前往我的订单继续支付",false,true);
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog3.dismiss();
                            finish();
                        }
                    },2000);
                }
                break;
        }
    }
}
