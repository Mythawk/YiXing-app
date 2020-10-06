package com.mythawk.yixing.bean;

public class OrderData {

    private String order_id;
    private String order_status;
    private double order_sum;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public double getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(double order_sum) {
        this.order_sum = order_sum;
    }
}
