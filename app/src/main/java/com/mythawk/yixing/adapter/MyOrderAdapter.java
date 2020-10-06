package com.mythawk.yixing.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mythawk.yixing.AddShareActivity;
import com.mythawk.yixing.MyOrderActivity;
import com.mythawk.yixing.PayActivity;
import com.mythawk.yixing.bean.OrderCount;
import com.mythawk.yixing.R;
import com.mythawk.yixing.bean.OrderData;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<OrderCount> orderCountList = new ArrayList<>();
    private int action = 0;
    private LayoutInflater inflater;
    private MyOrderActivity mContext;
    private int checkedItem = 0;

    public static final int ACTIONNOPAY = 1;
    public static final int ACTIONPAY = 2;
    public static final int ACTIONGet = 3;
    public static final int ACTIONRESULT = 4;
    public static final int ACTIONALL = 5;

    public MyOrderAdapter(List<OrderCount>orderList, int action , MyOrderActivity context) {
        this.orderCountList = orderList;
        this.action = action;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.template_my_order,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderCount orderCount = orderCountList.get(position);
        final OrderData orderData = orderCount.getOrderData();
        MyOrderListAdapter myOrderListAdapter =new MyOrderListAdapter(orderCount.getOrderList());
        holder.recyclerView.setAdapter(myOrderListAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        holder.txtOrderMoney.setText(String.valueOf(orderData.getOrder_sum()));
        holder.txtOrderNum.setText(orderData.getOrder_id());
        holder.txtStatus.setText(orderData.getOrder_status());
        switch (action){
            case ACTIONNOPAY:
                holder.btnFirst.setText("取消订单");
                holder.btnSecond.setText("支付订单");
                holder.btnFirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("确定要取消订单?");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //取消订单
                                mContext.sendAction(2,orderData.getOrder_id());
                            }
                        });

                        builder.setNegativeButton("不取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();  //创建AlertDialog对象
                        dialog.show();                           //显示对话框
                    }
                });
                holder.btnSecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String[]way = {"支付宝","微信支付"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("选择收货地址");
                        builder.setSingleChoiceItems(way, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkedItem = which;
                            }
                        });
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent =new Intent(mContext, PayActivity.class);
                                if (checkedItem==0){
                                    intent.putExtra("way","支付宝");
                                    intent.putExtra("id",orderData.getOrder_id());
                                }else {
                                    intent.putExtra("way","微信支付");
                                    intent.putExtra("id",orderData.getOrder_id());
                                }
                                intent.putExtra("sum",orderData.getOrder_sum());
                                mContext.startActivityForResult(intent,3);
                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();  //创建AlertDialog对象
                        dialog.show();                           //显示对话框


                    }
                });
                break;
            case ACTIONPAY:
            case ACTIONALL:
                holder.btnFirst.setVisibility(View.GONE);
                holder.btnSecond.setVisibility(View.GONE);
                break;
            case ACTIONGet:
                holder.btnFirst.setText("申请退货");
                holder.btnFirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("确定要申请退货?");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //申请退货
                                mContext.sendAction(4,orderData.getOrder_id());
                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();  //创建AlertDialog对象
                        dialog.show();                           //显示对话框
                    }
                });
                holder.btnSecond.setText("确认收货");
                holder.btnSecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("确定收货?");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //确认收货
                                mContext.sendAction(3,orderData.getOrder_id());
                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();  //创建AlertDialog对象
                        dialog.show();                           //显示对话框
                    }
                });
                break;
            case ACTIONRESULT:
                holder.btnFirst.setText("分享体验");
                holder.btnSecond.setVisibility(View.GONE);
                holder.btnFirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AddShareActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderCountList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtOrderNum;
        TextView txtStatus;
        TextView txtOrderMoney;
        RecyclerView recyclerView;
        Button btnFirst;
        Button btnSecond;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderNum = itemView.findViewById(R.id.txt_order_num);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtOrderMoney = itemView.findViewById(R.id.txt_order_money);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            btnFirst =itemView.findViewById(R.id.btn_first);
            btnSecond = itemView.findViewById(R.id.btn_second);
        }
    }

}
