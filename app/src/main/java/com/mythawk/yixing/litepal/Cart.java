package com.mythawk.yixing.litepal;

import org.litepal.crud.LitePalSupport;

public class Cart extends LitePalSupport {

    private int sneaker_id;
    private String cart_band;
    private String cart_shoes;
    private String cart_image;
    private double cart_price;
    private int cart_number;
    private int cart_size;

    public int getSneaker_id() {
        return sneaker_id;
    }

    public void setSneaker_id(int sneaker_id) {
        this.sneaker_id = sneaker_id;
    }
    public long getId(){
        return super.getBaseObjId();
    }

    public String getCart_band() {
        return cart_band;
    }

    public void setCart_band(String cart_band) {
        this.cart_band = cart_band;
    }

    public String getCart_image() {
        return cart_image;
    }

    public void setCart_image(String cart_image) {
        this.cart_image = cart_image;
    }

    public String getCart_shoes() {
        return cart_shoes;
    }

    public void setCart_shoes(String cart_shoes) {
        this.cart_shoes = cart_shoes;
    }

    public double getCart_price() {
        return cart_price;
    }

    public void setCart_price(double cart_price) {
        this.cart_price = cart_price;
    }

    public int getCart_number() {
        return cart_number;
    }

    public void setCart_number(int cart_number) {
        this.cart_number = cart_number;
    }

    public int getCart_size() {
        return cart_size;
    }

    public void setCart_size(int cart_size) {
        this.cart_size = cart_size;
    }
}
