package com.example.user.weather.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.weather.R;
import com.example.user.weather.UrlConfig.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeekWeatherFragment extends Fragment {

    private TextView mTextView;
    private static final String TAG = "WeekWeatherFragment";
    public static  String strUrl ;
    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ParsingData(msg.getData().getString("result"));
            }
        }
    };

    public static  WeekWeatherFragment newInstance(){
        WeekWeatherFragment weekWeatherFragment = new WeekWeatherFragment();
        strUrl = Constant.url +"CN101010100"+ Constant.key;
        return weekWeatherFragment;
    }

    public static  WeekWeatherFragment newInstance(String CityID){
        WeekWeatherFragment weekWeatherFragment = new WeekWeatherFragment();
        strUrl = Constant.url + CityID + Constant.key;
        return weekWeatherFragment;
    }

    public WeekWeatherFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_weather,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTextView = (TextView) getView().findViewById(R.id.tv_week_info);
        sendRequestWithOkHttp(strUrl);
        Log.d("request URL :",strUrl);
    }

    private void sendRequestWithOkHttp(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url).build();
                    Response response = client.newCall(request).execute();
                    //返回数据
                    String responseData = response.body().string();

                    Log.d("return Data",responseData);

                    Message msg =  new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("result",responseData);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void ParsingData(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            JSONObject FristData = jsonArray.getJSONObject(0);
//            Log.e("FristData",FristData.toString());

            JSONObject basic = new JSONObject(FristData.optString("basic"));
            JSONArray daily_forecast =FristData.getJSONArray("daily_forecast");


//            Log.e("daily_forecast", daily_forecast.toString());
            JSONObject SecondData = daily_forecast.getJSONObject(0);
//            Log.e("date", SecondData.optString("date"));
//            Log.e("tmp",new JSONObject(SecondData.optString("tmp")).get("min").toString());

            JSONObject tomorrow = daily_forecast.getJSONObject(1);
            JSONObject thirdDay = daily_forecast.getJSONObject(2);




            mTextView.setTextSize(35);
            mTextView.setText( "时间: " + SecondData.optString("date ")
                    + "温度: " + new JSONObject(SecondData.optString("tmp")).get("min") + "°/" + new JSONObject(SecondData.optString("tmp")).get("max") + "°"
                    +" 天气状况：" + new JSONObject(SecondData.optString("cond")).get("txt_d") +"\n"
                    +"时间: " + tomorrow.optString("date ")
                            + "温度: " + new JSONObject(tomorrow.optString("tmp")).get("min") + "°/" + new JSONObject(tomorrow.optString("tmp")).get("max") + "°"
                    +" 天气状况：" + new JSONObject(tomorrow.optString("cond")).get("txt_d") +"\n"
                    +"时间：" + thirdDay.optString("date ")
                    + "温度：" + new JSONObject(thirdDay.optString("tmp")).get("min") + "°/" + new JSONObject(thirdDay.optString("tmp")).get("max") + "°"
                    +" 天气状况：" + new JSONObject(thirdDay.optString("cond")).get("txt_d") +"\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
