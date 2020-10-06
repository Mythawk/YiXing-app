package com.mythawk.yixing.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mythawk.yixing.R;
import com.mythawk.yixing.litepal.Cart;
import com.mythawk.yixing.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Cart> cartList = new ArrayList<>();
    private LayoutInflater inflater;
    private ViewHolder holder;

    public OrderAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        holder = new ViewHolder(inflater.inflate(R.layout.template_order_wares,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(cart.getCart_image()));
        holder.textName.setText(cart.getCart_band()+" "+cart.getCart_shoes());
        holder.textPrice.setText(String.valueOf(cart.getCart_price()));
        holder.textSize.setText(String.valueOf(cart.getCart_size()));
        holder.textNumb.setText(String.valueOf(cart.getCart_number()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView simpleDraweeView;
        TextView textName;
        TextView textPrice;
        TextView textNumb;
        TextView textSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.drawee_view);
            textName = itemView.findViewById(R.id.txt_name);
            textPrice = itemView.findViewById(R.id.txt_price);
            textNumb = itemView.findViewById(R.id.text_numb);
            textSize = itemView.findViewById(R.id.text_size);
        }
    }


}
