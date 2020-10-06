package com.mythawk.yixing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mythawk.yixing.Gson.BaseBody;
import com.mythawk.yixing.Gson.SneakerBody;
import com.mythawk.yixing.bean.SendShare;
import com.mythawk.yixing.bean.Sneaker;
import com.mythawk.yixing.bean.SneakerPost;
import com.mythawk.yixing.litepal.Users;
import com.mythawk.yixing.utils.OkHttpUtil;
import com.mythawk.yixing.utils.Utility;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.mythawk.yixing.Contents.BASEURL;


public class AddShareActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ProgressDialog dialog;
    private List<String> nameList = new ArrayList<String>();
    private Spinner spinnerSneaker;
    private ArrayAdapter<String>spinnerAdapter;
    private EditText topicText;
    private EditText shareText;
    private Spinner spinnerShock;
    private Spinner spinnerParcel;
    private Spinner spinnerSupport;
    private Spinner spinnerGrip;
    private Spinner spinnerDurable;
    private Button imgBtn;
    private ImageView imageView;
    private Button sendButton;


    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private String uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_share);

        toolbar = findViewById(R.id.toolbar);
        spinnerSneaker = findViewById(R.id.spinnerSneaker);
        topicText = findViewById(R.id.topic);
        shareText = findViewById(R.id.share);
        spinnerShock =findViewById(R.id.spinnerShock);
        spinnerParcel = findViewById(R.id.spinnerParcel);
        spinnerSupport = findViewById(R.id.spinnerSupport);
        spinnerGrip = findViewById(R.id.spinnerGrip);
        spinnerDurable = findViewById(R.id.spinnerDurable);
        imgBtn = findViewById(R.id.imgBtn);
        imageView = findViewById(R.id.image);
        sendButton = findViewById(R.id.sendButton);


        nameList.add("未选择");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        requestData();
        sendButton.setOnClickListener(this) ;
        imgBtn.setOnClickListener(this);

    }




    private void requestData() {
        SneakerPost sneakerPost = new SneakerPost();
        sneakerPost.setMethod("all");
        sneakerPost.setContent("all");
        String url = BASEURL+"sneaker/redata";
        dialog = ProgressDialog.show(this,null,"正在加载...",false,true);
        OkHttpUtil.sendSneakerRequest(url, sneakerPost, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddShareActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final SneakerBody sneakerBody = Utility.handleSneakerResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(sneakerBody.status)&&sneakerBody!=null){
                            for (Sneaker sneaker:sneakerBody.sneakerList){
                                nameList.add(sneaker.getSneaker_name());

                                spinnerAdapter = new ArrayAdapter<String>(AddShareActivity.this,R.layout.simple_spinner_item,nameList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSneaker.setAdapter(spinnerAdapter);

                            }
                        }else {
                            Toast.makeText(AddShareActivity.this,"出现了错误！",Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendButton:
                Users users = LitePal.findFirst(Users.class);
                SendShare sendShare = new SendShare();
                sendShare.setTopic(topicText.getText().toString());
                sendShare.setShareText(shareText.getText().toString());
                sendShare.setName(users.getName());
                sendShare.setNumb(users.getNumb());
                sendShare.setSneakerName(spinnerSneaker.getSelectedItem().toString());
                if (spinnerShock.getSelectedItem().toString().equals("未选择")||spinnerParcel.getSelectedItem().toString().equals("未选择")||
                    spinnerSupport.getSelectedItem().toString().equals("未选择")||spinnerGrip.getSelectedItem().toString().equals("未选择")||spinnerDurable.getSelectedItem().toString().equals("未选择") ){
                    Toast.makeText(this,"还有未选择的评分项目！",Toast.LENGTH_SHORT).show();
                }else {
                    int shock = Integer.parseInt(spinnerShock.getSelectedItem().toString());
                    int parcel = Integer.parseInt(spinnerParcel.getSelectedItem().toString());
                    int support = Integer.parseInt(spinnerSupport.getSelectedItem().toString());
                    int grip = Integer.parseInt(spinnerGrip.getSelectedItem().toString());
                    int durable = Integer.parseInt(spinnerDurable.getSelectedItem().toString());
                    sendShare.setShock(shock);
                    sendShare.setParcel(parcel);
                    sendShare.setSupport(support);
                    sendShare.setGrip(grip);
                    sendShare.setDurable(durable);
                }
                if (uploadImage==null){
                    Toast.makeText(this,"请选择一张图片！",Toast.LENGTH_SHORT).show();
                }else {
                    String url = BASEURL + "share/addShare";
                    final Dialog dialog = ProgressDialog.show(AddShareActivity.this,null,"正在加载...",false,true);
                    OkHttpUtil.sendNewShare(url, sendShare, uploadImage, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( AddShareActivity.this,"出现错误!",Toast.LENGTH_SHORT).show();
                                    //dialog.dismiss();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            final BaseBody baseBody = Utility.handleAddShareResponse(responseText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("ok".equals(baseBody.status)){
                                        Toast.makeText(AddShareActivity.this,"发表成功！",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(AddShareActivity.this,"发表失败！请重试",Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            });

                        }
                    });
                }
                break;
            case R.id.imgBtn:
                chooseWay();
                break;
        }
    }

    private void uploadImage(String path){
        this.uploadImage = path;
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
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
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
            imageUri = FileProvider.getUriForFile(AddShareActivity.this,"com.example.cameraalbumtest.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    private void chooseImage(){
        if (ContextCompat.checkSelfPermission(AddShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddShareActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }

    private void chooseWay(){
        final CharSequence[] items = {"从相册上传","拍照上传"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddShareActivity.this);
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
        displayImage(imagePath);
        Log.d("test", "onActivityResult: "+imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        //上传图片
        uploadImage(imagePath);
        displayImage(imagePath);
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


    private void displayImage(String imagePath){
        if (imagePath !=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }

}
