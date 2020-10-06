package com.mythawk.yixing.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mythawk.yixing.R;
import com.mythawk.yixing.ShoesActivity;
import com.mythawk.yixing.bean.HomeData;
import com.mythawk.yixing.bean.Share;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private List<Share> shareList = new ArrayList<>();


    public HomeAdapter (List<Share> shareList){
        this.shareList = shareList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_home_cardview,parent,false));

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Share share = shareList.get(position);
        Glide.with(inflater.getContext()).load(share.getUser_image()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imgHead);
        Glide.with(inflater.getContext()).load(share.getImg()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imgMain);
        holder.txtHead.setText(share.getUser_name());
        holder.txtMain.setText(share.getShare_topic());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!= null){
                    onClickListener.onClick(holder.itemView,holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgHead;
        TextView txtHead;
        TextView txtMain;
        ImageView imgMain;

        final View view ;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHead = itemView.findViewById(R.id.img_head);
            txtHead = itemView.findViewById(R.id.txt_head);
            txtMain = itemView.findViewById(R.id.txt_main);
            imgMain = itemView.findViewById(R.id.img_main);

            view = itemView;
        }

    }

    public interface OnClickListener{
        void onClick(View itemView, int position);
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
