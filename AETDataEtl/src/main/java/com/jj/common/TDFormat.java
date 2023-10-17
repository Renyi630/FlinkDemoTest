package com.jj.common;

import com.alibaba.fastjson2.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TDFormat {
    public static String TDEventFormatter(JSONObject json){
        JSONObject standartJson = new JSONObject();

        String accountid = json.getString("uid");
        String event_name = json.getString("event_name");
        long event_time = json.getLong("event_time");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(event_time);
        standartJson.put("#account_id",accountid);
        standartJson.put("#time",sdf.format(date));
        standartJson.put("#event_name",event_name);
        standartJson.put("#type","track");

        json.remove("uid");
        json.remove("event_name");
        json.remove("event_time");

        standartJson.put("properties",json);

        JSONObject outputJson = new JSONObject();
        outputJson.put("debug",0);
        outputJson.put("appid","debug-appid");
        outputJson.put("data",standartJson);
        return outputJson.toJSONString();
    }

    public static String TDAetFormatter(JSONObject json){
        JSONObject standartJson = new JSONObject();

        String accountid = json.getString("UserId");
        String event_name = "AET1";
        long event_time = json.getLong("user_stime_z");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date = new Date(event_time);
        standartJson.put("#account_id",accountid);
        standartJson.put("#time",sdf.format(date));
        standartJson.put("#event_name",event_name);
        standartJson.put("#type","track");

        json.remove("uid");
        json.remove("event_name");
        json.remove("event_time");

        standartJson.put("properties",json);

        JSONObject outputJson = new JSONObject();
        outputJson.put("debug",0);
        outputJson.put("appid","debug-appid");
        outputJson.put("data",standartJson);

        return outputJson.toJSONString();
    }
}
