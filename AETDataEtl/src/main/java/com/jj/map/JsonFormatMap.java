package com.jj.map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jj.utils.LogUtils;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFormatMap implements MapFunction<String,JSONObject> {

//    private String regex;
//    public JsonFormatMap(String regex) {
//        this.regex = regex;
//    }

    @Override
    public JSONObject map(String input) throws Exception {
        Pattern pattern = Pattern.compile("(\\{)(.*)(\\})");
        Matcher matcher = pattern.matcher(input);
        JSONObject json = new JSONObject();
        if (matcher.find()) {
            String jsonData = matcher.group(0);
            try {
                json=JSON.parseObject(jsonData);
            } catch (Exception e) {
                LogUtils.error("dirty data:   "+jsonData);

                return json;
            }


            String[] lineArray = input.split(",", -1);
            if (lineArray.length >= 7) {
                String rcvtime = lineArray[0];
                String appname = lineArray[1];
                String ostype = lineArray[2];
                String version = lineArray[3];
                String iscompress = lineArray[4];
                String md5 = lineArray[5];
                json.put("rcvtime", rcvtime);
                json.put("appname", appname);
                json.put("ostype", ostype);
                json.put("version", version);
                json.put("iscompress", iscompress);
                json.put("md5", md5);
            }

        } else {
            LogUtils.error("数据匹配失败");
        }

        return json;
    }
}
