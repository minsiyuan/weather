package com.example.weather;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_response;
    private TextView address;
    private EditText cityid;
    private SharedPreferences sharedPreferences;
    private CityPicker mCP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = findViewById(R.id.check);
        sendRequest.setOnClickListener(this);
        Button Refresh = findViewById(R.id.refresh);
        Refresh.setOnClickListener(this);
        Button Choose = findViewById(R.id.click);
        Choose.setOnClickListener(this);
        Button Code = findViewById(R.id.citycode);
        Code.setOnClickListener(this);
        this.tv_response = findViewById(R.id.response);
        cityid = findViewById(R.id.cityid);
        address = findViewById(R.id.address);
    }
    //对天气字符串切片
    public String tranString(String rs){
        rs=rs.substring(rs.indexOf("[")+1,rs.indexOf("]"));
        Weather wt=JSONObject.parseObject(rs,Weather.class);
        return wt.toString();
    }
    //对城市字符串切片
    public String tranString1(String rs1){
        rs1=rs1.substring(rs1.indexOf("adcode")+7,rs1.indexOf("adcode")+13);
        return rs1;
    }
    //存储
    public void setSharedPreference(String cityid,String responeStr){
        sharedPreferences=getSharedPreferences("Weather",MODE_PRIVATE);
        //指定该 SharedPreferences 数据只能被本应用程序读、写。（默认值，值为 0 ）
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(cityid, responeStr);
        editor.commit();//将修改保存
    }
    //读取
    public String getSharedPreferences(String cityid) {
        sharedPreferences=getSharedPreferences("Weather",MODE_PRIVATE);
        String string=sharedPreferences.getString(cityid,"");
        //如果第一个参数没有找到对应的资源,则返回defValue设置的值
        return string;
    }
    public void initCityPicker() {
        //城市选择器
        mCP = new CityPicker.Builder(MainActivity.this)
                .textSize(10)//滚轮文字的大小
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("000000")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                .textColor(Color.parseColor("#0000CC"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(15)//滚轮item间距
                .onlyShowProvinceAndCity(false)
                .build();
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];

                address.setText(province + city + district);

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.check:
                if(!getSharedPreferences(cityid.getText().toString()).equals("")){
                    tv_response.setText(tranString(getSharedPreferences(cityid.getText().toString())));
                    ToastUtils.showToast(MainActivity.this, "缓存显示");
                }
                //初始化OKHttp客户端
                OkHttpClient client = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/weather/weatherInfo?city="+cityid.getText()+"&key=03fc3f746399384cea8ba203d478f14d&extensions=base")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败处理
                        ToastUtils.showToast(MainActivity.this, "请求失败");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //返回结果处理
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(responseStr.length()<100){
                                    ToastUtils.showToast(MainActivity.this, "代码输入有误");
                                }
                                else{
                                    tv_response.setText(tranString(responseStr));
                                    setSharedPreference(cityid.getText().toString(),responseStr);
                                }
                            }
                        });}
                });
                break;
            case R.id.refresh:
                //初始化OKHttp客户端
                OkHttpClient client1 = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request1 = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/weather/weatherInfo?city="+cityid.getText()+"&key=03fc3f746399384cea8ba203d478f14d&extensions=base")
                        .build();
                Call call1 = client1.newCall(request1);
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败处理
                        ToastUtils.showToast(MainActivity.this, "请求失败");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //返回结果处理
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(responseStr.length()<100){
                                    ToastUtils.showToast(MainActivity.this, "代码输入有误");
                                }
                                else{
                                    tv_response.setText(tranString(responseStr));
                                    ToastUtils.showToast(MainActivity.this, "已刷新");
                                }
                            }
                        });}
                });
                break;
            case R.id.click:
                initCityPicker();
                mCP.show();
                break;
            case R.id.citycode:
                //初始化OKHttp客户端
                OkHttpClient client2 = new OkHttpClient();
                //构造Request对象
                //采⽤建造者模式，链式调⽤指明进⾏Get请求,传⼊Get的请求地址
                Request request2 = new Request.Builder().get()
                        .url("https://restapi.amap.com/v3/geocode/geo?address="+address.getText()+"&output=XML&key=0a9b6dfd8d263cd415cf5f794d06f864&extensions=base")
                        .build();
                Call call2 = client2.newCall(request2);
                call2.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call2, IOException e) {
                        //失败处理
                        ToastUtils.showToast(MainActivity.this, "Get请求失败");
                    }
                    @Override
                    public void onResponse(Call call2, Response code) throws IOException {
                        //返回结果处理
                        final String responseStr2 = code.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cityid.setText(tranString1(responseStr2));
                            }

                        });}
                });
                break;
        }
    }

}
