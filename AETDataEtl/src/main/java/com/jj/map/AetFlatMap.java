package com.jj.map;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jj.common.TDFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class AetFlatMap implements FlatMapFunction<String, String> {
    @Override
    public void flatMap(String s, Collector<String> collector) throws Exception {
        List<JSONObject> list  = new ArrayList<JSONObject>();
        try {
            // null or '' check
            if (StringUtils.isNoneEmpty(s)) {
                if (s.contains("TrackVer")) {
//                            System.out.println("TrackVer："+s);

                    String BLANK = null;
                    // 用户展开
                    JSONObject jsonObject = JSON.parseObject(s);
                    List users = (List) jsonObject.get("Users");
                    List pages = (List) jsonObject.get("Pages");

                    if (users != null) {
                        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        for (Object user : users) {
                            JSONObject user_json = (JSONObject) user;
                            // refpages

                            if (user_json.get("UserId") != null) {
                                // Page展开
                                if (pages != null) {
                                    for (int i = 0; i < pages.size(); i++) {
                                        JSONObject page_json = (JSONObject) pages.get(i);

                                        JSONObject obj = new JSONObject();
                                        if (page_json.get("PageID")== "" || page_json.get("PageID")== null  ){
                                            continue;
                                        }
                                        obj.put("PageID", page_json.get("PageID") == null ? BLANK : page_json.get("PageID"));
                                        // source
                                        if (i == 0) {
                                            obj.put("RefPageID", "start");
                                        } else {
                                            JSONObject s_page = (JSONObject) pages.get(i - 1);
                                            obj.put("RefPageID", (s_page.get("PkgID") == null ? BLANK : s_page.get("PkgID")) + "#" + (s_page.get("GameID") == null ? BLANK : s_page.get("GameID")) + "#" + (s_page.get("PageID") == null ? BLANK : s_page.get("PageID")) + "#" + (s_page.get("CurViewName") == null ? BLANK : s_page.get("CurViewName")) + "#" + (s_page.get("WidgetName") == null ? BLANK : s_page.get("WidgetName")) + "#" + (s_page.get("actionName") == null ? BLANK : s_page.get("actionName")));
                                        }

                                        obj.put("AppID", jsonObject.get("AppID") == null ? BLANK : jsonObject.get("AppID"));
                                        obj.put("appname", jsonObject.get("appname") == null ? BLANK : jsonObject.get("appname"));
                                        obj.put("rcvtime", jsonObject.get("rcvtime") == null ? BLANK : jsonObject.get("rcvtime"));
                                        obj.put("TrackVer", jsonObject.get("TrackVer") == null ? BLANK : jsonObject.get("TrackVer"));
                                        obj.put("version", jsonObject.get("version") == null ? BLANK : jsonObject.get("version"));
                                        obj.put("iscompress", jsonObject.get("iscompress") == null ? BLANK : jsonObject.get("iscompress"));
                                        obj.put("md5", jsonObject.get("md5") == null ? BLANK : jsonObject.get("md5"));
                                        obj.put("ServerTime", jsonObject.get("ServerTime") == null ? BLANK : jsonObject.get("ServerTime"));
                                        obj.put("StartTime", jsonObject.get("StartTime") == null ? BLANK : jsonObject.get("StartTime"));
                                        obj.put("RLTime", jsonObject.get("RLTime") == null ? BLANK : jsonObject.get("RLTime"));
                                        obj.put("SessionID", jsonObject.get("SessionID") == null ? BLANK : jsonObject.get("SessionID"));
                                        // 计算时间
                                        BigInteger StartTime = jsonObject.getBigInteger("StartTime") == null ? BigInteger.valueOf(0) : jsonObject.getBigInteger("StartTime");
                                        BigInteger RLTime = jsonObject.getBigInteger("RLTime") == null ? BigInteger.valueOf(0) : jsonObject.getBigInteger("RLTime");
                                        // 基准时间
                                        BigInteger format_time = StartTime.subtract(RLTime);
                                        // 计算Session时间
                                        BigInteger session_stime_z = format_time.add(jsonObject.getBigInteger("STime") == null ? BigInteger.valueOf(0) : jsonObject.getBigInteger("STime"));
                                        BigInteger session_etime_z = format_time.add(jsonObject.getBigInteger("ETime") == null ? BigInteger.valueOf(0) : jsonObject.getBigInteger("ETime"));

                                        obj.put("session_stime_z", session_stime_z);
                                        obj.put("session_etime_z", session_etime_z);
                                        obj.put("session_stime", time.format(session_stime_z));
                                        obj.put("session_etime", time.format(session_etime_z));
                                        //obj.put("session_stime","");
                                        //obj.put("session_etime","");

                                        obj.put("UserId", user_json.get("UserId") == null ? BLANK : user_json.get("UserId"));
                                        obj.put("UserType", user_json.get("UserType") == null ? BLANK : user_json.get("UserType"));

                                        // 计算user时间
                                        BigInteger user_stime_z = format_time.add(user_json.getBigInteger("STime") == null ? BigInteger.valueOf(0) : user_json.getBigInteger("STime"));
                                        BigInteger user_etime_z = format_time.add(user_json.getBigInteger("ETime") == null ? BigInteger.valueOf(0) : user_json.getBigInteger("ETime"));

                                        obj.put("user_stime_z", user_stime_z);
                                        obj.put("user_etime_z", user_etime_z);
                                        obj.put("user_stime", time.format(user_stime_z));
                                        obj.put("user_etime", time.format(user_etime_z));
                                        //obj.put("user_stime","");
                                        //obj.put("user_etime","");

                                        obj.put("AppVer", jsonObject.get("AppVer") == null ? BLANK : jsonObject.get("AppVer"));
                                        obj.put("SDKVer", jsonObject.get("SDKVer") == null ? BLANK : jsonObject.get("SDKVer"));
                                        obj.put("MID", jsonObject.get("MID") == null ? BLANK : jsonObject.get("MID"));
                                        obj.put("ostype", jsonObject.get("ostype") == null ? BLANK : jsonObject.get("ostype"));
                                        obj.put("OS", jsonObject.get("OS") == null ? BLANK : jsonObject.get("OS"));
                                        obj.put("SysVer", jsonObject.get("SysVer") == null ? BLANK : jsonObject.get("SysVer"));
                                        obj.put("ChannelID", jsonObject.get("ChannelID") == null ? BLANK : jsonObject.get("ChannelID"));
                                        obj.put("CPromoter", jsonObject.get("CPromoter") == null ? BLANK : jsonObject.get("CPromoter"));
                                        obj.put("SiteID", jsonObject.get("SiteID") == null ? BLANK : jsonObject.get("SiteID"));
                                        obj.put("RealMac", jsonObject.get("RealMac") == null ? BLANK : jsonObject.get("RealMac"));
                                        obj.put("IMSI", jsonObject.get("IMSI") == null ? BLANK : jsonObject.get("IMSI"));
                                        obj.put("ICCID", jsonObject.get("ICCID") == null ? BLANK : jsonObject.get("ICCID"));
                                        obj.put("IMEI", jsonObject.get("IMEI") == null ? BLANK : jsonObject.get("IMEI"));
                                        obj.put("Processor", jsonObject.get("Processor") == null ? BLANK : jsonObject.get("Processor"));
                                        obj.put("Mac", jsonObject.get("Mac") == null ? BLANK : jsonObject.get("Mac"));
                                        obj.put("ScreenSize", jsonObject.get("ScreenSize") == null ? BLANK : jsonObject.get("ScreenSize"));
                                        obj.put("DeviceModel", jsonObject.get("DeviceModel") == null ? BLANK : jsonObject.get("DeviceModel"));
                                        obj.put("PhoneSize", jsonObject.get("PhoneSize") == null ? BLANK : jsonObject.get("PhoneSize"));
                                        //数数不能给空字符串
                                        obj.put("originalphonesize", jsonObject.get("originalphonesize") == null ? BLANK : jsonObject.get("originalphonesize"));
                                        obj.put("SDsize", jsonObject.get("SDsize") == null ? BLANK : jsonObject.get("SDsize"));
                                        obj.put("originalSDsize", jsonObject.get("originalSDsize") == null ? BLANK : jsonObject.get("originalSDsize"));
                                        obj.put("Nettype", jsonObject.get("Nettype") == null ? BLANK : jsonObject.get("Nettype"));
                                        obj.put("Operator", jsonObject.get("Operator") == null ? BLANK : jsonObject.get("Operator"));
                                        BigInteger page_stime_z = format_time.add(page_json.getBigInteger("STime") == null ? BigInteger.valueOf(0) : page_json.getBigInteger("STime"));
                                        BigInteger page_etime_z = format_time.add(page_json.getBigInteger("ETime") == null ? BigInteger.valueOf(0) : page_json.getBigInteger("ETime"));

                                        // 计算page时间
                                        obj.put("page_stime_z", page_stime_z);
                                        obj.put("page_etime_z", page_etime_z);
                                        obj.put("page_stime", time.format(page_stime_z));
                                        obj.put("page_etime", time.format(page_etime_z));
                                        //obj.put("page_stime","");
                                        //obj.put("page_etime","");


                                        obj.put("PkgID", page_json.get("PkgID") == null ? BLANK : ""+page_json.get("PkgID"));
                                        obj.put("GameID", page_json.get("GameID") == null ? BLANK : page_json.get("GameID"));
                                        obj.put("PageID", page_json.get("PageID") == null ? BLANK : page_json.get("PageID"));
                                        obj.put("PkgVer", page_json.get("PkgVer") == null ? BLANK : page_json.get("PkgVer"));
                                        obj.put("WidgetName", page_json.get("WidgetName") == null ? BLANK : page_json.get("WidgetName"));
                                        obj.put("WidgetResult", "");
                                        obj.put("CurViewName", page_json.get("CurViewName") == null ? BLANK : page_json.get("CurViewName"));
                                        obj.put("actionName", page_json.get("actionName") == null ? BLANK : page_json.get("actionName"));
                                        obj.put("behavior_xtr_enough", "false");
                                        obj.put("behavior_xtr_isfree", "");
                                        obj.put("behavior_xtr_levelid", "");
                                        obj.put("behavior_xtr_opm_cnt", "");
                                        obj.put("behavior_xtr_paymoney", "");
                                        list.add(obj);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR Line:" + s);
        }

        for (JSONObject jsonObject : list) {
            String jsonString = TDFormat.TDAetFormatter(jsonObject);
            collector.collect(jsonString);
        }
    }
}

