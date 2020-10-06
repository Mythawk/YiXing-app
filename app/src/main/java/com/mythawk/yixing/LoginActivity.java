package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mythawk.yixing.Gson.UsersBody;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.ClearEditText;
import com.mythawk.yixing.widget.CnToolbar;

import java.io.IOException;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{



    public static final int ACTION_LOGIN=1;
    public static final int ACTION_REGISTERED=2;
    public static final int ACTION_FORGET=3;
    private int action = ACTION_LOGIN;

    private CnToolbar cnToolbar;
    private ClearEditText loginPhone;
    private ClearEditText loginNumb;
    private LinearLayout linearLayout;
    private LinearLayout loginRegisteredLayout;
    private Button loginButton;
    private EditText registeredTxt;
    private Button registeredButton;
    private TextView textView1;
    private TextView textView2;

    private ProgressDialog dialog;

    private String phoneNumber;
    private String checkCode ;
    private Users users;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cnToolbar = findViewById(R.id.toolbar);
        loginPhone = findViewById(R.id.login_phone);
        loginNumb = findViewById(R.id.login_pwd);
        linearLayout = findViewById(R.id.registered_layout);
        loginRegisteredLayout = findViewById(R.id.login_registered_layout);
        loginButton = findViewById(R.id.btn_login);
        registeredTxt = findViewById(R.id.registered_txt);
        registeredButton = findViewById(R.id.registered_button);
        textView1 = findViewById(R.id.txt_toReg);
        textView2 = findViewById(R.id.txt_toFog);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registeredButton.setOnClickListener(this);

        initToolbar();
        ChangeView();

        SMSSDK.registerEventHandler(eh);
    }

    private void ChangeView() {

        initToolbar();

        switch (action){

            case ACTION_LOGIN:
                linearLayout.setVisibility(View.GONE);
                loginNumb.setHint(R.string.login_pwd);
                loginButton.setText(R.string.login_logins);
                textView1.setText(R.string.login_forget_button);
                textView2.setText(R.string.login_registered_button);
                break;
            case ACTION_REGISTERED:
                linearLayout.setVisibility(View.VISIBLE);
                loginNumb.setHint(R.string.login_pwd);
                loginButton.setText(R.string.login_registered_button);
                textView1.setText(R.string.login);
                textView2.setText(R.string.login_forget_button);
                break;
            case ACTION_FORGET:
                linearLayout.setVisibility(View.VISIBLE);
                loginNumb.setHint(R.string.login_new_pwd);
                loginButton.setText(R.string.login_logins);
                textView1.setText(R.string.login_registered_button);
                textView2.setText(R.string.login);
                break;
        }

    }

    private void initToolbar() {

        switch (action){

            case ACTION_LOGIN:
                cnToolbar.setTitle(R.string.login_logins);
                break;
            case ACTION_REGISTERED:
                cnToolbar.setTitle(R.string.login_registers);
                break;
            case ACTION_FORGET:
                cnToolbar.setTitle(R.string.login_forgets);
                break;
        }

        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_toReg:
                ChangeAction(1);
                ChangeView();
                break;
            case R.id.txt_toFog:
                ChangeAction(2);
                ChangeView();
                break;
            case R.id.btn_login:
                String numb = null ;
                int pwd = 0;
                int txt = 0;


                numb =  loginPhone.getText().toString();
                 if (TextUtils.isEmpty(numb)) {
                     Toast.makeText(this, "号码不能为空", Toast.LENGTH_SHORT).show();
                 }
                String pwds =loginNumb.getText().toString() ;
                if (pwds == null){
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    pwd=Integer.parseInt(pwds);
                }

                checkCode = registeredTxt.getText().toString() ;
                phoneNumber = numb;

                switch (action){
                    case ACTION_LOGIN:
                        //登录
                        users = new Users();
                        users.setNumb(numb);
                        users.setPwd(pwd);

                        String url = BASEURL+"users/redata";
                        OkHttpUtil.sendActionRequest(url,users, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"登录失败!",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseText = response.body().string();
                                final UsersBody usersBack = Utility.handleLoginResponse(responseText);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (usersBack !=null && "ok".equals(usersBack.status)){
                                            Users users1 = usersBack.users;
                                            users1.save();
                                            Toast.makeText(LoginActivity.this,"登录成功!",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {
                                            Toast.makeText(LoginActivity.this,"登录失败！因为账号或密码错误!",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                        });

                        break;
                    case ACTION_REGISTERED:
                    case ACTION_FORGET:
                        //忘记密码
                        //注册
                        users = new Users();
                        users.setNumb(numb);
                        users.setPwd(pwd);


                        if (TextUtils.isEmpty(checkCode)){
                            Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                        }else {
                            dialog = ProgressDialog.show(this,null,"正在验证...",false,true);
                            //提交短信验证码
                            SMSSDK.submitVerificationCode("86",phoneNumber,checkCode);
                        }

                        break;


                }
                break;
            case R.id.registered_button://点击发送验证码

                phoneNumber = loginPhone.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(this,"手机号码不能为空!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this,"已发送验证码!",Toast.LENGTH_SHORT).show();
                    SMSSDK.getVerificationCode("86",phoneNumber);
                }
                timer = new CountDownTimer( 60000,1000 ) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        registeredButton.setText((millisUntilFinished/1000)+"秒后再次发送");
                        registeredButton.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        registeredButton.setText("再次发送");
                        registeredButton.setEnabled(true);
                    }
                }.start();
            break;
        }

    }

    private void ChangeAction(int i) {
        switch (i){
            case 1:
                action--;
                break;
            case 2:
                action++;
        }
        if (action == 0 ){
            action =  ACTION_FORGET;
        }else if (action == 4){
            action = ACTION_LOGIN;
        }
    }
    //注册监听
    private EventHandler eh =new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    HashMap<String, Object> mData = (HashMap<String, Object>) data;
                    String country = (String) mData.get("country");//返回的国家编号
                    String phone = (String) mData.get("phone");//返回用户注册的手机号
                    Log.e("LogintActivity:", country + "====" + phone);
                    if (phone.equals(phoneNumber)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //返回主线程，向服务器添加用户
                                new Thread(new MyThread()).start();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDailog("验证失败");
                                dialog.dismiss();
                            }
                        });
                    }
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };
    private void showDailog(String text) {
        new AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("确定", null)
                .show();
    }

    private class MyThread implements Runnable{
        @Override
        public void run() {
            switch (action){
                case ACTION_REGISTERED:
                    String url = BASEURL+"users/registered";
                    OkHttpUtil.sendActionRequest(url,users, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"注册失败!",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            final UsersBody usersBack = Utility.handleLoginResponse(responseText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (usersBack !=null && "ok".equals(usersBack.status)){
                                        Users users1 = usersBack.users;
                                        users1.save();
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this,"注册成功!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this,"注册失败:用户已存在!",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                }
                            });
                        }
                    });
                    break;
                case ACTION_FORGET:
                    url = BASEURL+"users/forget";
                    OkHttpUtil.sendActionRequest(url,users, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            final UsersBody usersBack = Utility.handleLoginResponse(responseText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (usersBack !=null && "ok".equals(usersBack.status)){
                                        Users users1 = usersBack.users;
                                        users1.save();
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
                                        finish();

                                    }else {
                                        Toast.makeText(LoginActivity.this,"修改失败，用户不存在!",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                }
                            });
                        }
                    });
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
        if (timer!=null){
            timer.cancel();
        }
    }
}
