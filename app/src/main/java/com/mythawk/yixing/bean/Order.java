package com.mythawk.yixing.bean;

public class Order {

    private String order_id;
    private String user_number;
    private int sneaker_number;
    private String sneaker_name;
    private String sneaker_image;
    private double sneaker_price;
    private int sneaker_size;
    private String address_name;
    private String address_phone;
    private String address_address;
    private String address_detail;
    private String order_note;
    private String order_status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }




    public String getSneaker_name() {
        return sneaker_name;
    }

    public void setSneaker_name(String sneaker_name) {
        this.sneaker_name = sneaker_name;
    }

    public String getSneaker_image() {
        return sneaker_image;
    }

    public void setSneaker_image(String sneaker_image) {
        this.sneaker_image = sneaker_image;
    }


    public int getSneaker_number() {
        return sneaker_number;
    }

    public void setSneaker_number(int sneaker_number) {
        this.sneaker_number = sneaker_number;
    }

    public double getSneaker_price() {
        return sneaker_price;
    }

    public void setSneaker_price(double sneaker_price) {
        this.sneaker_price = sneaker_price;
    }

    public int getSneaker_size() {
        return sneaker_size;
    }

    public void setSneaker_size(int sneaker_size) {
        this.sneaker_size = sneaker_size;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_phone() {
        return address_phone;
    }

    public void setAddress_phone(String address_phone) {
        this.address_phone = address_phone;
    }

    public String getAddress_address() {
        return address_address;
    }

    public void setAddress_address(String address_address) {
        this.address_address = address_address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getOrder_note() {
        return order_note;
    }

    public void setOrder_note(String order_note) {
        this.order_note = order_note;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
