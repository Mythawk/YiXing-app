package com.mythawk.yixing.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.ComData;


import java.util.List;


public class ComAdapter extends RecyclerView.Adapter<ComAdapter.ViewHolder> {

    private List<ComData> comDataList;
    private LayoutInflater inflater;

    public ComAdapter(List<ComData> comDataList) {
        this.comDataList = comDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ComAdapter.ViewHolder(inflater.inflate(R.layout.template_com_list,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComData comData = comDataList.get(position);
        Glide.with(inflater.getContext()).load(comData.getCom_img()).into(holder.circleImageView);
        holder.textClock.setText(comData.getCom_time());
        holder.comFloor.setText(String.valueOf(comData.getCom_floor()));
        holder.txtHead.setText(comData.getCom_name());
        holder.comText.setText(comData.getCom_text());
    }

    @Override
    public int getItemCount() {
        return comDataList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView txtHead;
        TextView textClock;
        TextView comText;
        TextView comFloor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.img_head);
            txtHead = itemView.findViewById(R.id.txt_head);
            textClock = itemView.findViewById(R.id.txt_clock);
            comText = itemView.findViewById(R.id.com_txt);
            comFloor = itemView.findViewById(R.id.com_floor);
        }

    }

}
