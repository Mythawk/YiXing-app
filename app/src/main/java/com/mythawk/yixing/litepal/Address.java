package com.mythawk.yixing.litepal;

import org.litepal.crud.LitePalSupport;

public class Address extends LitePalSupport {

    private String name;
    private String phone;
    private String address;
    private String detail;

    public Address(String name, String phone, String address, String detail) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.detail = detail;
    }

    public Address() {
    }

    public long getID(){
        return super.getBaseObjId();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
