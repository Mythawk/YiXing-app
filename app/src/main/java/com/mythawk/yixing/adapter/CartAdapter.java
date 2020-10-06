package com.mythawk.yixing.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mythawk.yixing.R;
import com.mythawk.yixing.litepal.Cart;
import com.mythawk.yixing.widget.NumberAddSubView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Cart> cartList = new ArrayList<>();
    private ViewHolder viewHolder;
    private OnClickListener onClickListener;
    private boolean ischeck =false;

    public CartAdapter( List<Cart> cartList ){
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        viewHolder = new ViewHolder(inflater.inflate(R.layout.template_cart,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);
        holder.nameText.setText(cart.getCart_band()+" "+cart.getCart_shoes());
        holder.priceText.setText(String.valueOf(cart.getCart_price()));
        holder.draweeView.setImageURI(Uri.parse(cart.getCart_image()));
        holder.numberAddSubView.setValue(cart.getCart_number());
        holder.sizeText.setText("码数: "+cart.getCart_size());
        holder.numberAddSubView.setMaxValue(9);
        holder.numberAddSubView.setMinValue(1);
        holder.numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                cart.setCart_number(value);
                cart.save();
                onClickListener.updateText();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                cart.setCart_number(value);
                cart.save();
                onClickListener.updateText();
            }
        });
        holder.checkBox.setChecked(ischeck);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onClickListener!= null){
                    onClickListener.onClick(isChecked,holder.getLayoutPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        SimpleDraweeView draweeView;
        TextView nameText;
        TextView priceText;
        NumberAddSubView numberAddSubView;
        TextView sizeText;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            draweeView = itemView.findViewById(R.id.drawee_view);
            nameText = itemView.findViewById(R.id.text_title);
            priceText = itemView.findViewById(R.id.text_price);
            sizeText = itemView.findViewById(R.id.text_size);
            numberAddSubView = itemView.findViewById(R.id.num_control);

        }

    }

    public void setCheckBoxAll(boolean isCheck){
        this.ischeck=isCheck;
        int itemCount = cartList.size();
        notifyItemRangeChanged(0,itemCount);
    }

    public void finishDelt(){
        int itemCount = cartList.size();
        notifyItemRangeChanged(0,itemCount);
    }

    public interface OnClickListener{
        void onClick(boolean isChecked, int position);
        void updateText();
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }



}
