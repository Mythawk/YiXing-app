package com.mythawk.yixing.adapter;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.DraweeView;
import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.Rank;

import java.util.ArrayList;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private List<Rank> rankList = new ArrayList<>();

    public RankAdapter(List<Rank> rankList){
        this.rankList = rankList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_rank_listview,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Rank rank = rankList.get(position);
        holder.imageView.setImageURI(Uri.parse(rank.getRank_img()));
        holder.textNumb.setText(String.valueOf(position+1));
        holder.textScout.setText(String.valueOf(rank.getRank_scout()));
        holder.textName.setText(rank.getRank_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textNumb;
        DraweeView imageView;
        TextView textName;
        TextView textScout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumb = itemView.findViewById(R.id.rankNumb);
            textName = itemView.findViewById(R.id.rankName);
            imageView = itemView.findViewById(R.id.rankImg);
            textScout = itemView.findViewById(R.id.rankScout);

        }

    }



    public interface OnClickListener{
        void onClick(View itemView, int position);
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
