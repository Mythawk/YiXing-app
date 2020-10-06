package com.mythawk.yixing.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.mythawk.yixing.bean.ComData;
import com.mythawk.yixing.bean.Order;
import com.mythawk.yixing.bean.SendShare;
import com.mythawk.yixing.bean.SneakerPost;
import com.mythawk.yixing.litepal.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {

    public static void sendActionRequest(String address, Users users, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("numb",users.getNumb());
            json.put("pwd",users.getPwd());
            json.put("name",users.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendSneakerRequest(String address, SneakerPost sneakerPost, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("method",sneakerPost.getMethod());
            json.put("content",sneakerPost.getContent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }
    public static void sendShoesRequest(String address, String name , okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void sendRankRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendShareRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendShareRequest(String address, int id , okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }


    public static void sendImage(String address,String numb,String imagePath,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Log.d("imagePath:", imagePath);
        //使用bitmap构建图片
        BitmapFactory.Options options =  new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //压缩图片并上传
        bitmap.compress(Bitmap.CompressFormat.PNG,50,stream);
        byte[] byte_arr = stream.toByteArray();
        String encodeString = Base64.encodeToString(byte_arr,0);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("image",encodeString);
            json.put("numb",numb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    //发表新体验
    public static void sendNewShare(String address, SendShare sendShare, String imagePath, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Log.d("imagePath:", imagePath);
        //使用bitmap构建图片
        BitmapFactory.Options options =  new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //压缩图片并上传
        bitmap.compress(Bitmap.CompressFormat.PNG,50,stream);
        byte[] byte_arr = stream.toByteArray();
        String encodeString = Base64.encodeToString(byte_arr,0);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("image",encodeString);
            json.put("numb",sendShare.getNumb());
            json.put("name",sendShare.getName());
            json.put("sneakerName",sendShare.getSneakerName());
            json.put("topic",sendShare.getTopic());
            json.put("shareText",sendShare.getShareText());
            json.put("shock",sendShare.getShock());
            json.put("parcel",sendShare.getParcel());
            json.put("support",sendShare.getSupport());
            json.put("grip",sendShare.getGrip());
            json.put("durable",sendShare.getDurable());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendComRequest(String address, ComData comData, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("share_id",comData.getShare_id());
            json.put("com_num",comData.getCom_num());
            json.put("com_text",comData.getCom_text());
            json.put("com_floor",comData.getCom_floor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void sendAddOrderRequest(String address, List<Order> orderList , Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {

            String orderData = com.alibaba.fastjson.JSON.toJSONString(orderList);
            Log.d("OkHttp:", "sendAddOrderRequest: "+orderData);
            json.put("data",orderData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void sendSelectOrderRequest(String address,String phone, String status , Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("status",status);
            json.put("numb",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public static void sendEditOrderRequest(String address,String orderId, int action , Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("action",action);
            json.put("orderId",orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody =  RequestBody.create(JSON,String.valueOf(json));

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

}
