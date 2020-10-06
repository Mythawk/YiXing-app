package com.mythawk.yixing.litepal;

import org.litepal.crud.LitePalSupport;

public class CategoryItem  extends LitePalSupport {

    private int cate;
    private String name;
    private int imgId;

    public long getID(){
        return super.getBaseObjId();
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
