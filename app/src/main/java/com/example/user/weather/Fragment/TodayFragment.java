package com.example.user.weather.Fragment;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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

public class TodayFragment extends Fragment {

    private TextView mTextView;
    private static final String TAG = "TodayFragment";
    private static String strUrl ;
    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ParsingData(msg.getData().getString("result"));
            }
        }
    };

    public static  TodayFragment newInstance(){
        TodayFragment todayFragment = new TodayFragment();
        strUrl = Constant.url +"CN101010100"+ Constant.key;
        return todayFragment;
    }

    public static  TodayFragment newInstance(String CityID){
        TodayFragment todayFragment = new TodayFragment();
        strUrl = Constant.url + CityID + Constant.key;
        return todayFragment;
    }

    public TodayFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTextView = (TextView) getView().findViewById(R.id.tv_today_info);
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



            JSONObject suggestion = new JSONObject(FristData.optString("suggestion"));
            JSONObject airJY = new JSONObject(suggestion.optString("air"));
            JSONObject comf = new  JSONObject(suggestion.optString("comf"));
            JSONObject cw = new  JSONObject(suggestion.optString("cw"));
            JSONObject drsg = new  JSONObject(suggestion.optString("drsg"));
            JSONObject uv = new  JSONObject(suggestion.optString("uv"));


//            Log.e("suggestion:",suggestion.toString());
//            Log.e("air",airJY.optString("brf"));
//            Log.e("air",airJY.optString("txt"));
            mTextView.setTextSize(35);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setText("城市: " + basic.optString("city")
            + "\n\n" + "时间: " + SecondData.optString("date") + "\n"
                    + "温度: " + new JSONObject(SecondData.optString("tmp")).get("min") + "°/" + new JSONObject(SecondData.optString("tmp")).get("max")+"°\n" +
                    "天气状况：" + new JSONObject(SecondData.optString("cond")).get("txt_d") + "\n"
            +"生活建议: " +"\n空气: "+airJY.optString("brf")+ airJY.optString("txt") +"\n"
                    +"舒适度：" + comf.optString("brf") + comf.optString("txt") + "\n"
                    +"洗车：" + cw.optString("brf") + cw.optString("txt") + "\n"
                    +"着装：" + drsg.optString("brf") + drsg.optString("txt") + "\n"
                    +"紫外线：" + uv.optString("brf") + uv.optString("txt") + "\n"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
