package com.mythawk.yixing.bean;

public class Tab {

    private  int txt;
    private int img;
    private Class fragment;

    public Tab(int txt, int img, Class fragment) {
        this.txt = txt;
        this.img = img;
        this.fragment = fragment;
    }

    public int getTxt() {
        return txt;
    }

    public void setTxt(int txt) {
        this.txt = txt;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
