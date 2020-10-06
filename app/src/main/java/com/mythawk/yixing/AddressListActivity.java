package com.mythawk.yixing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.mythawk.yixing.bean.ProvinceBean;
import com.mythawk.yixing.litepal.Address;
import com.mythawk.yixing.utils.JsonFileReader;
import com.mythawk.yixing.widget.ClearEditText;
import com.mythawk.yixing.widget.CnToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {

    private CnToolbar cnToolbar;
    private ClearEditText editName;
    private ClearEditText editPhone;
    private TextView textAddress;
    private ClearEditText editAddress;

    private int data;
    private boolean isAdd;


    OptionsPickerView pvOptions;
    //  省份
    ArrayList<String> provinceBeanList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        cnToolbar = findViewById(R.id.toolbar);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editAddress = findViewById(R.id.edit_address);
        textAddress =findViewById(R.id.txt_address);

        data = getIntent().getIntExtra("data",0);
        isAdd = getIntent().getBooleanExtra("add",false);
        Address address = new Address();
        address = LitePal.find(Address.class,data);

        textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });

        initToolbar();
        initEditData(address);
        initOption();

    }

    private void initOption() {

        //  获取json数据
        String province_data_json = JsonFileReader.getJson(this, "province.json");
        //  解析json数据
        parseJson(province_data_json);

        //  创建选项选择器
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String city = provinceBeanList.get(options1);
                String address; //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                    address = provinceBeanList.get(options1) + "-" + districtList.get(options1).get(options2).get(options3);
                } else {
                    address = provinceBeanList.get(options1) + "-" + cityList.get(options1).get(options2) + "-" + districtList.get(options1).get(options2).get(options3);
                    //address = provinceBeanList.get(options1) + "-" + cityList.get(options1).get(options2);
                }
                textAddress.setText(address);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色
                .setBgColor(Color.WHITE)//滚轮背景颜色
                .setContentTextSize(20)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();

        pvOptions.setPicker(provinceBeanList, cityList, districtList);//添加数据源

    }

    private void initEditData(Address address) {
        if (isAdd != true){
            //添加数据
            editName.setText(address.getName());
            editPhone.setText(address.getPhone());
            editAddress.setText(address.getDetail());
            textAddress.setText(address.getAddress());
        }
    }

    private void initToolbar() {
        cnToolbar.hideSearchView();
        cnToolbar.showTitleView();
        cnToolbar.setNavigationIcon(R.mipmap.icon_back_button);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isAdd == true){
            cnToolbar.setTitle(R.string.address_add);
        }else {
            cnToolbar.setTitle(R.string.address_manager);
        }
        cnToolbar.setRightButtonText(R.string.address_sure);
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdd != true){
                    Address address = LitePal.find(Address.class,data);
                    address.setName(editName.getText().toString());
                    address.setPhone(editPhone.getText().toString());
                    address.setAddress(textAddress.getText().toString());
                    address.setDetail(editAddress.getText().toString());
                    if (address.getName() == null || address.getPhone()==null||address.getAddress()==null||address.getDetail()==null){
                        Toast.makeText(AddressListActivity.this,"还有未填项！",Toast.LENGTH_SHORT).show();
                    }else {
                        address.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }else {
                    Address address = new Address();
                    address.setName(editName.getText().toString());
                    address.setPhone(editPhone.getText().toString());
                    address.setAddress(textAddress.getText().toString());
                    address.setDetail(editAddress.getText().toString());
                    Log.d("test", "onClick: "+address.getName());
                    if (address.getName().isEmpty() || address.getPhone().isEmpty() || address.getAddress().isEmpty() || address.getDetail().isEmpty()){
                        Toast.makeText(AddressListActivity.this,"还有未填项！",Toast.LENGTH_SHORT).show();
                    }else {
                        address.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }

            }
        });
    }


    //  解析json填充集合
    public void parseJson(String str) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(str);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                JSONObject provinceObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String provinceName = provinceObject.getString("name");
                provinceBeanList.add(provinceName);
                //  获取城市数组
                JSONArray cityArray = provinceObject.optJSONArray("city");
                cities = new ArrayList<>();//   声明存放城市的集合
                districts = new ArrayList<>();//声明存放区县集合的集合
                //  遍历城市数组
                for (int j = 0; j < cityArray.length(); j++) {
                    //  获取城市对象
                    JSONObject cityObject = cityArray.optJSONObject(j);
                    //  将城市放入集合
                    String cityName = cityObject.optString("name");
                    cities.add(cityName);
                    district = new ArrayList<>();// 声明存放区县的集合
                    //  获取区县的数组
                    JSONArray areaArray = cityObject.optJSONArray("area");
                    //  遍历区县数组，获取到区县名称并放入集合
                    for (int k = 0; k < areaArray.length(); k++) {
                        String areaName = areaArray.getString(k);
                        district.add(areaName);
                    }
                    //  将区县的集合放入集合
                    districts.add(district);
                }
                //  将存放区县集合的集合放入集合
                districtList.add(districts);
                //  将存放城市的集合放入集合
                cityList.add(cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
