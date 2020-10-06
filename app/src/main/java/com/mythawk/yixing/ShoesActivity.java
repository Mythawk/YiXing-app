package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mythawk.yixing.Gson.ShoesBody;
import com.mythawk.yixing.adapter.ShoesAdapter;
import com.mythawk.yixing.litepal.Cart;
import com.mythawk.yixing.bean.Scout;
import com.mythawk.yixing.bean.Share;
import com.mythawk.yixing.bean.Sneaker;
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

public class ShoesActivity extends AppCompatActivity implements View.OnClickListener {

    private CnToolbar cnToolbar;
    private ImageView sneakerImage;
    private TextView sneakerBand;
    private TextView sneakerName;
    private TextView sneakerPrice;
    private Button buyButton;
    private Sneaker sneakerData;
    private TextView shoesShock;
    private TextView shoesParcel;
    private TextView shoesSupport;
    private TextView shoesGrip;
    private TextView shoesDurable;
    private TextView shoesScout;
    private RecyclerView recyclerView;
    private TextView noTextView;
    private Spinner spinnerSize;
    private TextView getSize;

    private Dialog dia;

    private Scout scout = new Scout();
    private Sneaker sneaker = new Sneaker();
    private List<Share> shareList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shose);

        cnToolbar = findViewById(R.id.toolbar);
        sneakerImage = findViewById(R.id.shoesPhoto);
        sneakerBand = findViewById(R.id.shoesBand);
        sneakerName = findViewById(R.id.shoesName);
        sneakerPrice = findViewById(R.id.shoesPrice);
        buyButton = findViewById(R.id.shoesBuy);
        shoesShock = findViewById(R.id.shock);
        shoesParcel = findViewById(R.id.parcel);
        shoesSupport = findViewById(R.id.support);
        shoesGrip   = findViewById(R.id.grip);
        shoesDurable = findViewById(R.id.durable);
        shoesScout = findViewById(R.id.average);
        recyclerView = findViewById(R.id.recycler_view);
        noTextView = findViewById(R.id.no_text);
        spinnerSize = findViewById(R.id.spinner);
        getSize = findViewById(R.id.getSizeText);

        buyButton.setOnClickListener(this);
        getSize.setOnClickListener(this);


        dia = new Dialog(this,R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.activity_start_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
        imageView.setBackgroundResource(R.mipmap.shoes_size);
        //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
        dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.onWindowAttributesChanged(lp);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });

        initToolbar();

        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String url = BASEURL + "scout/getScoutByName";

        OkHttpUtil.sendShoesRequest(url, name, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShoesActivity.this,"获取失败!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final ShoesBody shoesBody = Utility.handleShoesResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scout = shoesBody.scoutData;
                        sneaker = shoesBody.sneakerData;
                        shareList = shoesBody.shareData;

                        sneakerBand.setText(String.valueOf(sneaker.getSneaker_band()));
                        sneakerName.setText(String.valueOf(sneaker.getSneaker_name()));
                        sneakerPrice.setText(String.valueOf(sneaker.getSneaker_price()));
                        sneakerData = sneaker;
                        shoesShock.setText(String.valueOf(scout.getShock()));
                        shoesDurable.setText(String.valueOf(scout.getDurable()));
                        shoesGrip.setText(String.valueOf(scout.getGrip()));
                        shoesParcel.setText(String.valueOf(scout.getParcel()));
                        shoesSupport.setText(String.valueOf(scout.getSupport()));
                        shoesScout.setText(String.valueOf(scout.getScout()));
                        Glide.with(ShoesActivity.this).load(sneaker.getSneaker_img()).into(sneakerImage);

                        if (shareList != null){
                            recyclerView.setVisibility(View.VISIBLE);
                            ShoesAdapter shoesAdapter = new ShoesAdapter(shareList);
                            shoesAdapter.setOnClickListener(new ShoesAdapter.OnClickListener() {
                                @Override
                                public void onClick(View itemView, int position) {
                                    Intent intent =new Intent(ShoesActivity.this, MainShareActivity.class);
                                    intent.putExtra("data",shareList.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(shoesAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ShoesActivity.this));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                        }else {
                            noTextView.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        });

    }

    private void initToolbar() {
        cnToolbar.showTitleView();
        cnToolbar.hideSearchView();
        cnToolbar.setTitle("商品详情");
        cnToolbar.setNavigationIcon(R.mipmap.icon_back_button);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cnToolbar.setRightButtonIcon(R.mipmap.icon_cart);
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ShoesActivity.this,MainActivity.class);
                boolean isToCart = true;
                intent.putExtra("toCart" ,isToCart);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shoesBuy:
                List<Cart> cartList = LitePal.where("cart_shoes = ? and cart_size = ?",sneaker.getSneaker_name(),spinnerSize.getSelectedItem().toString()).find(Cart.class);
                if (cartList.size() != 0 ){
                    Cart cart = cartList.get(0);
                    cart.setCart_number(cart.getCart_number()+1);
                    cart.save();
                }else {
                    Cart cart = new Cart();
                    cart.setSneaker_id(sneaker.getSneaker_id());
                    cart.setCart_shoes(sneaker.getSneaker_name());
                    cart.setCart_band(sneaker.getSneaker_band());
                    cart.setCart_price(sneaker.getSneaker_price());
                    cart.setCart_image(sneaker.getSneaker_img());
                    cart.setCart_number(1);
                    cart.setCart_size(Integer.parseInt(spinnerSize.getSelectedItem().toString()));
                    cart.save();
                }
                Toast.makeText(ShoesActivity.this,"已添加到购物车",Toast.LENGTH_SHORT).show();
                break;
            case R.id.getSizeText:
                dia.show();
                break;
        }
    }

}
