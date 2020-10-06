package com.mythawk.yixing.Gson;

import com.google.gson.annotations.SerializedName;
import com.mythawk.yixing.bean.Sneaker;

import java.util.List;

public class SneakerBody {

    public String status;

    @SerializedName("redata")
    public List<Sneaker> sneakerList;

}
