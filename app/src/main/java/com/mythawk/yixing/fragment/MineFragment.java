package com.mythawk.yixing.fragment;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mythawk.yixing.AddressActivity;
import com.mythawk.yixing.LoginActivity;
import com.mythawk.yixing.MainActivity;
import com.mythawk.yixing.MyOrderActivity;
import com.mythawk.yixing.PersonalActivity;
import com.mythawk.yixing.R;
import com.mythawk.yixing.litepal.Users;

import org.litepal.LitePal;


import de.hdodenhof.circleimageview.CircleImageView;


public class MineFragment extends Fragment implements View.OnClickListener{


    private CircleImageView imgHead;
    private TextView txtUsername;
    private Button logoutButton;
    private TextView btnPersonal;
    private TextView txtAddress;
    private TextView txtMyOrder;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private Users users;

   // private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);

        imgHead = view.findViewById(R.id.img_head);
        txtUsername = view.findViewById(R.id.txt_username);
        logoutButton = view.findViewById(R.id.btn_logout);
        btnPersonal = view.findViewById(R.id.button_personal);
        txtAddress = view.findViewById(R.id.txt_address);
        txtMyOrder = view.findViewById(R.id.txt_my_orders);

        imgHead.setOnClickListener(this);
        txtUsername.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        txtAddress.setOnClickListener(this);
        txtMyOrder.setOnClickListener(this);

        isLogin();

        return view;
    }

    private void isLogin() {

        users = LitePal.findFirst(Users.class);
        if(users != null){
            txtUsername.setText(users.getName());
            logoutButton.setVisibility(View.VISIBLE);
            btnPersonal.setVisibility(View.VISIBLE);
            showImg(users);
            txtUsername.setEnabled(false);
            imgHead.setEnabled(false);
            btnPersonal.setOnClickListener(this);
        }else {
            imgHead.setImageResource(R.drawable.default_head);
            btnPersonal.setVisibility(View.INVISIBLE);
            txtUsername.setText(R.string.login);
            logoutButton.setVisibility(View.GONE);
            txtUsername.setEnabled(true);
            imgHead.setEnabled(true);
        }
    }

    private void showImg(Users users){
        imgHead.setImageResource(R.drawable.default_head);
        String userImg = users.getImage();
        Glide.with(getActivity()).load(userImg).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgHead);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_head:
                Intent intent = new Intent( getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_username:
                intent = new Intent( getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                LitePal.deleteAll(Users.class);
                super.getActivity().finish();
                intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_personal:
                Intent intent2 = new Intent(getContext(), PersonalActivity.class);
                String img = users.getImage();
                String name = users.getName();
                intent2.putExtra("img",img);
                intent2.putExtra("name",name);
                startActivity(intent2);
                break;
            case R.id.txt_address:
                Intent intent3 = new Intent(getContext(), AddressActivity.class);
                startActivity(intent3);
                break;
            case R.id.txt_my_orders:
                Users users = LitePal.findFirst(Users.class);
                if (users != null) {
                    Intent intent4 = new Intent(getContext(), MyOrderActivity.class);
                    startActivity(intent4);
                }else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        isLogin();
    }
}
