package com.mythawk.yixing.bean;

public class HomeData {

    private int imgHeadId;
    private String txtHead;
    private String txtMain;
    private int imgMainId;

    public HomeData(int imgHeadId, String txtHead, String txtMain, int imgMainId) {
        this.imgHeadId = imgHeadId;
        this.txtHead = txtHead;
        this.txtMain = txtMain;
        this.imgMainId = imgMainId;
    }

    public int getImgHeadId() {
        return imgHeadId;
    }

    public void setImgHeadId(int imgHeadId) {
        this.imgHeadId = imgHeadId;
    }

    public String getTxtHead() {
        return txtHead;
    }

    public void setTxtHead(String txtHead) {
        this.txtHead = txtHead;
    }

    public String getTxtMain() {
        return txtMain;
    }

    public void setTxtMain(String txtMain) {
        this.txtMain = txtMain;
    }

    public int getImgMainId() {
        return imgMainId;
    }

    public void setImgMainId(int imgMainId) {
        this.imgMainId = imgMainId;
    }
}
