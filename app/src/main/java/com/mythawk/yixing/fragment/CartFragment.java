package com.mythawk.yixing.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mythawk.yixing.LoginActivity;
import com.mythawk.yixing.OrderActivity;
import com.mythawk.yixing.R;
import com.mythawk.yixing.adapter.CartAdapter;
import com.mythawk.yixing.litepal.Cart;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment  implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;
    private Button mBtnOrder;
    private Button mBtnDel;
    private CnToolbar mToolbar;

    private static final int ACTION_EDIT=1;
    private static final int ACTION_CAMPLATE=2;

    private List<Cart> cartList = new ArrayList<>();
    private CartAdapter mCartAdaper;
    private List<Cart> sum = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mTextTotal = (TextView) view.findViewById(R.id.txt_total);
        mBtnDel = (Button) view.findViewById(R.id.btn_del);
        mBtnOrder = (Button)view.findViewById(R.id.btn_order);
        mToolbar = view.findViewById(R.id.toolbar);

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartList.removeAll(sum);
                for (Cart cart :sum){
                    LitePal.delete(Cart.class,cart.getId());
                }
                mCartAdaper.finishDelt();
                onStart();
            }
        });
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                ArrayList<String> data = new ArrayList<String>();
                for (Cart cart : sum){
                    data.add(String.valueOf(cart.getId()));
                }
                if (data.size()==0){
                    Toast.makeText(getContext(),"请选选择需要购买的物品",Toast.LENGTH_SHORT).show();
                }else {
                    intent.putStringArrayListExtra("data",data);
                    startActivity(intent);
                }
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
                if (isCheck && cartList.size() != sum.size() ){
                    sum.clear();
                    sum.addAll(cartList);
                    setCheckAll(isCheck);
                }else if (!isCheck && cartList.size()==sum.size()){
                    sum.clear();
                    setCheckAll(isCheck);
                }else if (isCheck && cartList.size() == sum.size()){
                    sum.clear();
                    sum.addAll(cartList);
                }
                updataText();
            }
        });

        initToolbar();
        showData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refData();
        updataText();
    }

    private void showData() {
        cartList = LitePal.findAll(Cart.class);
        mCartAdaper = new CartAdapter(cartList);
        mCartAdaper.setOnClickListener(new CartAdapter.OnClickListener() {
            @Override
            public void onClick(boolean isChecked, int position) {
                if (isChecked){
                    sum.add(cartList.get(position));
                }else {
                    sum.remove(cartList.get(position));
                }
                updataText();
                Log.d("Cart:", "onCheckedChanged: "+sum.toString());
                if (sum.size() == cartList.size()){
                    mCheckBox.setChecked(true);
                }else {
                    mCheckBox.setChecked(false);
                }
            }

            @Override
            public void updateText() {
                updataText();
            }
        });
        mRecyclerView.setAdapter(mCartAdaper);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void refData(){
        sum.clear();
        cartList.clear();
        cartList = LitePal.findAll(Cart.class);
        showData();
    }

    private void initToolbar() {

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);

    }


    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if(ACTION_EDIT == action){
            showDelControl();
            sum.clear();
        }
        else if(ACTION_CAMPLATE == action){
            hideDelControl();
            sum.clear();
        }


    }

    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);
        mCheckBox.setChecked(false);
    }

    private void  hideDelControl(){

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mCheckBox.setChecked(false);
    }

    public void updataText(){
        float sumBumb = 0;
        for (Cart cart: sum){
            sumBumb += cart.getCart_price()*cart.getCart_number();
        }
        mTextTotal.setText("合计:"+sumBumb);
    }

    private void setCheckAll(boolean isCheck){
        final  Boolean isChecked = isCheck;
        if(mRecyclerView.isComputingLayout()){
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mCartAdaper.setCheckBoxAll(isChecked);
                }
            });
        }else {
            mCartAdaper.setCheckBoxAll(isCheck);
        }

    }

}
