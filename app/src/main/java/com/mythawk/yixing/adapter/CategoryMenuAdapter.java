package com.mythawk.yixing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.CategoryMenuItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryMenuAdapter extends RecyclerView.Adapter<CategoryMenuAdapter.ViewHolder> {


    private List<CategoryMenuItem> mCategoryMenuItem = new ArrayList<>();
    private OnClickListener onClickListener;


    public CategoryMenuAdapter(List<CategoryMenuItem> categoryMenuItemList) {
        this.mCategoryMenuItem = categoryMenuItemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_category_menu,parent,false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CategoryMenuItem categoryMenuItem = mCategoryMenuItem.get(position);
        holder.imageView.setImageResource(categoryMenuItem.getImgId());
        holder.textView.setText(categoryMenuItem.getName());
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

        return mCategoryMenuItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView ;
        TextView textView;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_menu_img);
            textView = itemView.findViewById(R.id.category_menu_txt);
            view = itemView;

        }
    }


    public interface OnClickListener{
        void onClick(View itemView, int position);
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public void clear(){
        int itemCount = mCategoryMenuItem.size();
        mCategoryMenuItem.clear();
        this.notifyItemRangeRemoved(0,itemCount);
    }
    public void addData(List<CategoryMenuItem> datas){

        addData(0,datas);
    }
    public void addData(int position,List<CategoryMenuItem> datas){
        if(datas !=null && datas.size()>0) {
            this.mCategoryMenuItem.addAll(datas);
            this.notifyItemRangeChanged(position, datas.size());
        }
    }

}
