package com.mythawk.yixing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mythawk.yixing.Gson.UsersBody;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;
import com.mythawk.yixing.widget.CnToolbar;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private CnToolbar cnToolbar;
    private FrameLayout layoutImage;
    private FrameLayout layoutName;
    private ImageView btn_next;
    private LinearLayout nameEdit;
    private Button editBtn;
    private EditText editText;
    private CircleImageView imgHead;
    private TextView myName;

    private String img;
    private String name;
    private Users users = new Users();
    private Boolean action = false;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        cnToolbar = findViewById(R.id.toolbar);
        layoutImage = findViewById(R.id.layoutImage);
        layoutName = findViewById(R.id.layoutName);
        btn_next = findViewById(R.id.btn_next);
        nameEdit = findViewById(R.id.nameEdit);
        editBtn = findViewById(R.id.editBtn);
        editText = findViewById(R.id.editName);
        imgHead = findViewById(R.id.img_head);
        myName = findViewById(R.id.my_name);

        layoutName.setOnClickListener(this);
        layoutImage.setOnClickListener(this);
        editBtn.setOnClickListener(this);

        nameEdit.setLayoutTransition(new LayoutTransition());

        getdata();
        initToolbar();

    }

    private void initToolbar() {
        cnToolbar.showTitleView();
        cnToolbar.hideSearchView();
        cnToolbar.setTitle(R.string.my_person);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });
    }

    private void getdata() {
        Intent intent = getIntent();
        img = intent.getStringExtra("img");
        name = intent.getStringExtra("name");
        users = LitePal.findFirst(Users.class);
        if (img != null){
            Glide.with(PersonalActivity.this).load(img).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgHead);
        }
        if (name != null){
            myName.setText(name);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutName:
                if (action){
                    action = false;
                    nameEdit.setVisibility(View.GONE);
                    btn_next.animate().rotation(0);
                }else {
                    action = true;
                    nameEdit.setVisibility(View.VISIBLE);
                    btn_next.animate().rotation(90);
                }
                break;
            case R.id.layoutImage:
                chooseWay();
                break;
            case R.id.editBtn:
                editName();
                break;
        }
    }

    private void editName() {
        final ProgressDialog progressDialog = new ProgressDialog(PersonalActivity.this);
        progressDialog.setTitle("上传中。。。");
        progressDialog.setCancelable(true);
        progressDialog.show();
        String url = BASEURL +"users/editName";
        users.setName(editText.getText().toString());
        Log.d("Test", "editName: "+editText.getText().toString());
        OkHttpUtil.sendActionRequest(url, users, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalActivity.this,"上传失败!",Toast.LENGTH_SHORT).show();
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
                            LitePal.deleteAll(Users.class);
                            Users users1 = usersBack.users;
                            users1.save();
                            Toast.makeText(PersonalActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(PersonalActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });


    }

    private void choosePhoto() {
        File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >=24){
            imageUri = FileProvider.getUriForFile(PersonalActivity.this,"com.example.cameraalbumtest.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    private void chooseImage(){
        if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PersonalActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent =new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"未授权",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {

                        Log.d("test", "onActivityResult: "+imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imgHead.setImageBitmap(bitmap);
                        Log.d("test", "onActivityResult: "+imageUri.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String path = "/storage/emulated"+imageUri.getPath();
                    uploadImage(path);
                }
                break;
            case CHOOSE_PHOTO:
                Log.d("test", "onActivityResult: 相册放回");
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT>=19){
                        //4.4及以上使用这个办法
                        handleImageOnKitKat(data);
                    }else {
                        //4.4及以下使用这个办法
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void chooseWay(){
        final CharSequence[] items = {"从相册上传","拍照上传"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalActivity.this);
        dialog.setTitle("请选择方式");
        dialog.setCancelable(true);
        dialog.setItems(items, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    chooseImage();
                }else {
                    choosePhoto();
                }
            }
        }).create();

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private void uploadImage(String imageUri){
        Log.d("Test:", "uploadImage: "+imageUri);
        final ProgressDialog progressDialog = new ProgressDialog(PersonalActivity.this);
        progressDialog.setTitle("上传中。。。");
        progressDialog.setCancelable(true);
        progressDialog.show();
        String uri = BASEURL+"users/editImage";
        OkHttpUtil.sendImage(uri, users.getNumb(), imageUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalActivity.this,"上传失败!",Toast.LENGTH_SHORT).show();
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
                            LitePal.deleteAll(Users.class);
                            Users users1 = usersBack.users;
                            users1.save();
                            Toast.makeText(PersonalActivity.this,"上传成功!",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(PersonalActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID +"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的Uri,则使用普通处理方式
                imagePath = getImagePath(uri,null);
            }else if ("file".equalsIgnoreCase(uri.getScheme())){
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
        }
        //上传图片
        uploadImage(imagePath);
        Log.d("test", "onActivityResult: "+imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        //上传图片
        uploadImage(imagePath);
        Log.d("test", "onActivityResult: "+imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path =null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.Media.DATA
                ));
            }
            cursor.close();
        }
        return path;
    }
}
