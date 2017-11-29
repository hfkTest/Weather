package com.example.user.weather;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.weather.Fragment.TodayFragment;
import com.example.user.weather.Fragment.WeekWeatherFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnToday,btnWeek,btnSearch;
    private Fragment fragment;
    private Fragment Weekfragment;
    private EditText etCity;

    private void  init(){
        btnToday = (Button) findViewById(R.id.btn_today);
        btnWeek = (Button) findViewById(R.id.btn_week);
        btnSearch = (Button) findViewById(R.id.btn_Search);
        btnToday.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

//    private void JSONDataTest() {
//        try {
//            JSONObject jsonObject  = new JSONObject(JSONData);
//            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
//            JSONObject FristData = jsonArray.getJSONObject(0);
//            Log.e("FristData",FristData.toString());
//
//            JSONObject basic = new JSONObject(FristData.optString("basic"));
//            Log.e("city",basic.optString("city"));
//            Log.e("cnty",basic.optString("cnty"));
//
//            JSONArray daily_forecast =FristData.getJSONArray("daily_forecast");
//
//
//            Log.e("daily_forecast", daily_forecast.toString());
//            JSONObject SecondData = daily_forecast.getJSONObject(0);
//            Log.e("date", SecondData.optString("date"));
//            Log.e("tmp",new JSONObject(SecondData.optString("tmp")).get("min").toString());
//
//            JSONObject ThirdData = daily_forecast.getJSONObject(1);
//            Log.e("date", ThirdData.optString("date"));
//            Log.e("tmp",new JSONObject(ThirdData.optString("tmp")).get("min").toString());
//
//            JSONObject suggestion = new JSONObject(FristData.optString("suggestion"));
//            JSONObject airJY = new JSONObject(suggestion.optString("air"));
//            Log.e("suggestion:",suggestion.toString());
//            Log.e("air",airJY.optString("brf"));
//            Log.e("air",airJY.optString("txt"));
//
////          Log.e("date",daily_forecast.optString("date"));
////          Log.e("temp",new JSONObject(daily_forecast.optString("tmp")).optString("max"));
////          Log.e("temp",new JSONObject(daily_forecast.optString("tmp")).optString("min"));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_today:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fragment = TodayFragment.newInstance();
                transaction.replace(R.id.ly_realContent,fragment);
                transaction.commit();
                //JSONDataTest();
                break;
            case R.id.btn_week:
                FragmentTransaction Weektransaction = getSupportFragmentManager().beginTransaction();
                Weekfragment = WeekWeatherFragment.newInstance();
                Weektransaction.replace(R.id.ly_realContent,Weekfragment);
                Weektransaction.commit();
                break;
            case R.id.btn_Search:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("查找城市");
                etCity = new EditText(this);
                builder.setView(etCity);
                builder.setPositiveButton("提交",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    QueryCityIdHelper queryCityIdHelper = new QueryCityIdHelper();
                    String CityID = queryCityIdHelper.getCityID(etCity.getText().toString().trim());
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    fragment = TodayFragment.newInstance(CityID);
                    transaction.replace(R.id.ly_realContent,fragment);
                    transaction.commit();
                }
            });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

    }


}
