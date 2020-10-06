package com.mythawk.yixing.bean;

public class Rank {

    private int id;
    private String rank_name;
    private String rank_img;
    private float rank_scout;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public String getRank_img() {
        return rank_img;
    }

    public void setRank_img(String rank_img) {
        this.rank_img = rank_img;
    }

    public float getRank_scout() {
        return rank_scout;
    }

    public void setRank_scout(float rank_scout) {
        this.rank_scout = rank_scout;
    }
}
