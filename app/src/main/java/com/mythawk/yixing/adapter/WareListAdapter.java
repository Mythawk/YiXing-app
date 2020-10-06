package com.mythawk.yixing.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.DraweeView;
import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.Sneaker;

import java.util.ArrayList;
import java.util.List;

public class WareListAdapter  extends RecyclerView.Adapter<WareListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private List<Sneaker> sneakerList = new ArrayList<>();
    private int layoutResId ;//= R.layout.template_warelist_hot;

    public WareListAdapter(  List<Sneaker> sneakerList ,int layoutId){
        this.sneakerList = sneakerList;
        this.layoutResId = layoutId;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(layoutResId,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Sneaker sneaker = sneakerList.get(position);
        holder.draweeView.setImageURI(Uri.parse(sneaker.getSneaker_img()));
        Log.d("", "onBindViewHolder: "+Uri.parse(sneaker.getSneaker_img()));
        holder.txt_name.setText(sneaker.getSneaker_name());
        holder.txt_band.setText(sneaker.getSneaker_band());
        holder.txt_price.setText(String.valueOf(sneaker.getSneaker_price()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(holder.itemView,holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sneakerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        DraweeView draweeView;
        TextView txt_band;
        TextView txt_name;
        TextView txt_price;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            draweeView = itemView.findViewById(R.id.drawee_view);
            txt_band = itemView.findViewById(R.id.text_band);
            txt_name = itemView.findViewById(R.id.text_name);
            txt_price = itemView.findViewById(R.id.text_price);
        }

    }

    public interface OnClickListener{
        void onClick(View itemView, int position);
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void refreshData(List<Sneaker> list){

        if(list !=null && list.size()>0){

            int itemCount = sneakerList.size();
            sneakerList.clear();
            notifyItemRangeChanged(0,itemCount);
            int size = list.size();
            for (int i=0;i<size;i++){
                sneakerList.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }

}
