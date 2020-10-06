package com.mythawk.yixing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.CategoryMainItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.ViewHolder> {

    private List<CategoryMainItem> categoryMainItemList = new ArrayList<>();
    private LayoutInflater inflater;

    private OnClickListener onClickListener;

    public CategoryMainAdapter(  List<CategoryMainItem> categoryMainItems ) {
        this.categoryMainItemList = categoryMainItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_category_main,parent,false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CategoryMainItem categoryMainItem = categoryMainItemList.get(position);
        holder.textView.setText(categoryMainItem.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
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
        return categoryMainItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_mainTxt);
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
