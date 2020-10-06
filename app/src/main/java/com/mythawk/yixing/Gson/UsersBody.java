package com.mythawk.yixing.Gson;

import com.google.gson.annotations.SerializedName;
import com.mythawk.yixing.litepal.Users;


public class UsersBody {

    public String status;

    @SerializedName("redata")
    public Users users;

}
