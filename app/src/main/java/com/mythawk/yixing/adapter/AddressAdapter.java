package com.mythawk.yixing.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mythawk.yixing.AddressActivity;
import com.mythawk.yixing.AddressListActivity;
import com.mythawk.yixing.R;
import com.mythawk.yixing.litepal.Address;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<Address> addressList =new ArrayList<>();
    private LayoutInflater inflater;


    public AddressAdapter(List<Address> addressList) {
        this.addressList = addressList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_address,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        final Address address = addressList.get(position);
        holder.txtName.setText(address.getName());
        holder.txtPhone.setText(address.getPhone());
        holder.txtAddress.setText(address.getAddress()+" "+address.getDetail() );
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(holder.txtDel,position);
            }
        });
        holder.txtDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.delete(Address.class,address.getID());
                addressList = LitePal.findAll(Address.class);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtPhone;
        TextView txtAddress;
        TextView txtEdit;
        TextView txtDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtEdit = itemView.findViewById(R.id.txt_edit);
            txtDel = itemView.findViewById(R.id.txt_del);
        }
    }

    private OnClickListener onClickListener;

    public interface OnClickListener{
        void onClick(View itemView, int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
