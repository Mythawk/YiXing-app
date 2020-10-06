package com.mythawk.yixing.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mythawk.yixing.Gson.BaseBody;
import com.mythawk.yixing.Gson.ComBody;
import com.mythawk.yixing.Gson.OrderBody;
import com.mythawk.yixing.bean.OrderCount;
import com.mythawk.yixing.Gson.RankBody;
import com.mythawk.yixing.Gson.ShareBody;
import com.mythawk.yixing.Gson.ShareIdBody;
import com.mythawk.yixing.Gson.ShoesBody;
import com.mythawk.yixing.Gson.SneakerBody;
import com.mythawk.yixing.Gson.UsersBody;


import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /*
     * 处理登录返回的数据
     */
    public static UsersBody handleLoginResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "handleLoginResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                //JSONArray jsonArray = jsonObject.getJSONArray("redata");
                //String usersContent = jsonArray.getJSONObject(0).toString();
                String usersContent = jsonObject.toString();
                return new Gson().fromJson(usersContent,UsersBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理品牌返回的数据
     */
    public static SneakerBody handleSneakerResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "SneakerResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                //JSONArray jsonArray = jsonObject.getJSONArray("redata");
                String SneakerContent = jsonObject.toString();
                return new Gson().fromJson(SneakerContent,SneakerBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    /*
     * 处理运动鞋返回的数据
     */
    public static ShoesBody handleShoesResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "ShoesResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                //JSONArray jsonArray = jsonObject.getJSONArray("redata");
                String SneakerContent = jsonObject.toString();
                return new Gson().fromJson(SneakerContent,ShoesBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理主页面返回的数据
     */
    public static ShareBody handleShareResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "ShareResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String ShareContent = jsonObject.toString();
                return new Gson().fromJson(ShareContent,ShareBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理详细页面返回的数据
     */
    public static ShareIdBody handleShareByIdResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "ShareByIdResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String ShareContent = jsonObject.toString();
                return new Gson().fromJson(ShareContent, ShareIdBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    /*
     * 处理主业面返回的数据
     */
    public static RankBody handleRankResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "RankResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                //JSONArray jsonArray = jsonObject.getJSONArray("redata");
                String RankContent = jsonObject.toString();
                return new Gson().fromJson(RankContent,RankBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    /*
     * 处理添加体验返回的数据
     */
    public static BaseBody handleAddShareResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "AddShareResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String RankContent = jsonObject.toString();
                return new Gson().fromJson(RankContent,BaseBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理查询数据返回的数据
     */
    public static ComBody handleComRequestResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "ComRequestResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String comContent = jsonObject.toString();
                return new Gson().fromJson(comContent,ComBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理生成订单返回的数据
     */
    public static BaseBody handleAddOrderResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "AddOrderResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String orderContent = jsonObject.toString();
                return new Gson().fromJson(orderContent,BaseBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理查询订单返回的数据
     */

    public static OrderBody handleSelectOrderResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "SelectOrderResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String orderContent = jsonObject.toString();
                return new Gson().fromJson(orderContent, OrderBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 处理修改订单返回的数据
     */
    public static BaseBody handleEditOrderResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                Log.d("Util:", "EditOrderResponse: "+response);
                JSONObject jsonObject = new JSONObject(response);
                String orderContent = jsonObject.toString();
                return new Gson().fromJson(orderContent,BaseBody.class);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }


}
