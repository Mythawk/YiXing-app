package com.mythawk.yixing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mythawk.yixing.adapter.AddressAdapter;
import com.mythawk.yixing.litepal.Address;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    private CnToolbar cnToolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        cnToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);

        initToolbar();
        getData();
    }

    private void getData() {
        final List<Address> addressList  = LitePal.findAll(Address.class);
        AddressAdapter addressAdapter = new AddressAdapter(addressList);
        addressAdapter.setOnClickListener(new AddressAdapter.OnClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                Intent intent =new Intent(AddressActivity.this,AddressListActivity.class);
                intent.putExtra("data",(int) addressList.get(position).getID());
                startActivityForResult(intent,1);
            }
        });
        recyclerView.setAdapter(addressAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (addressList==null){
            Toast.makeText(this,"请添加收货地址!",Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar() {
        cnToolbar.hideSearchView();
        cnToolbar.showTitleView();
        cnToolbar.setTitle(R.string.address_tittle);
        cnToolbar.setNavigationIcon(R.mipmap.icon_back_button);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cnToolbar.setRightButtonText(R.string.address_new);
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this,AddressListActivity.class);
                intent.putExtra("add",true);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    getData();
                }
                break;
        }
    }
}
