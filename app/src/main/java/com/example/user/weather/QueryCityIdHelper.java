package com.example.user.weather;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/11/24.
 */

public class QueryCityIdHelper {

    private Map<String,String> CityID = new HashMap<String, String>() ;

    public QueryCityIdHelper(){
        CityID.put("shanghai","CN101020100");
        CityID.put("guangzhou","CN101280101");
        CityID.put("shenzhen","CN101280601");
        CityID.put("suzhou","CN101190401");
        CityID.put("nanning","CN101300101");
    }

    public String getCityID(String key) {
        return CityID.get(key);
    }
}
