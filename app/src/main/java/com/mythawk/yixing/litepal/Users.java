package com.mythawk.yixing.litepal;

import org.litepal.crud.LitePalSupport;

public class Users extends LitePalSupport {

    private String numb;
    private int pwd;
    private String name;
    private String image;

    public long getID(){
        return super.getBaseObjId();
    }

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public int getPwd() {
        return pwd;
    }

    public void setPwd(int pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
