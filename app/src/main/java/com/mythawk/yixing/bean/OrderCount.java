package com.mythawk.yixing.bean;

import com.mythawk.yixing.bean.Order;
import com.mythawk.yixing.bean.OrderData;

import java.util.List;

public class OrderCount {

    private OrderData orderData;
    private List<Order> orderList;

    public OrderCount(OrderData orderData, List<Order> orderList) {
        this.orderData = orderData;
        this.orderList = orderList;
    }

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
