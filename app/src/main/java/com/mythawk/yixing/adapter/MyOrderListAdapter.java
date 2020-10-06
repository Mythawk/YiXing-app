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
import com.mythawk.yixing.bean.Order;

import java.util.List;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.ViewHolder> {

    private List<Order> orderList;
    private LayoutInflater inflater;
    private ViewHolder holder;

    public MyOrderListAdapter(List<Order> orderList) {
        this.orderList = orderList;
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
        Order order = orderList.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(order.getSneaker_image()));
        holder.textName.setText(order.getSneaker_name());
        holder.textPrice.setText(String.valueOf(order.getSneaker_price()));
        holder.textSize.setText(String.valueOf(order.getSneaker_size()));
        holder.textNumb.setText(String.valueOf(order.getSneaker_number()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
