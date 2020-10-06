package com.mythawk.yixing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.ComData;
import com.mythawk.yixing.bean.Share;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShoesAdapter extends RecyclerView.Adapter<ShoesAdapter.ViewHolder> {

    private List<Share> shareList;
    private LayoutInflater inflater;
    private OnClickListener onClickListener;

    public ShoesAdapter(List<Share> shareList) {
        this.shareList = shareList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_com_list,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Share share = shareList.get(position);
        Glide.with(inflater.getContext()).load(share.getUser_image()).into(holder.circleImageView);
        holder.textClock.setText(share.getDate());
        holder.comFloor.setVisibility(View.INVISIBLE);
        holder.txtHead.setText(share.getUser_name());
        holder.comText.setText(share.getShare_topic());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("test:", "getItemCount: "+shareList.size());
        return shareList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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

    public interface OnClickListener{
        void onClick(View itemView, int position);
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
