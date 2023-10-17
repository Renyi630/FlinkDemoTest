package com.jj.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author fyj
 */
public class ParseJson {
    /**
     * 嵌套的字段都带外层key
     * @param jsonLine
     * @param jsonMap
     * @param initKey
     */
    public static void getDetailData(String jsonLine,Map jsonMap,String initKey){
        if (StringUtils.isNotBlank(jsonLine)){
            JSONObject json = JSONObject.parseObject(jsonLine);
            for(Object k : json.keySet()){
                Object v = json.get(k);
                //如果内层还是JSONObject的话,继续解析
                if (v instanceof JSONObject){
                    getDetailData(v.toString(),jsonMap,k.toString());
                }else{
                    if (jsonMap.containsKey(k.toString())) {
                        jsonMap.put(initKey+"."+k.toString(), v);
                    }else {
                        if (initKey.equals("initKey")) {
                            jsonMap.put(k.toString(), v);
                        }else{
                            jsonMap.put(initKey+"."+k.toString(), v);
                        }
                    }
                }
            }
        }

    }

    /**
     * 不同名key保留  同名key添加外层key作为新的key
     * @param jsonLine
     * @param jsonMap
     */
    public static void getDetailData(String jsonLine,Map jsonMap){
        if (StringUtils.isNotBlank(jsonLine)){
            //先解析最外层
            JSONObject json = JSONObject.parseObject(jsonLine);
            for(Object k : json.keySet()){
                Object v = json.get(k);
                if (StringUtils.isBlank(v.toString())){
                    jsonMap.put(k.toString(), "null");
                }else {
                    jsonMap.put(k.toString(), v);
                }
            }
            getAnyInnerData(jsonMap);
        }
    }

    /**
     * 解析嵌套
     * @param jsonLine
     * @param jsonMap
     * @param initKey
     */
    private static void getInnerData2(String jsonLine,Map jsonMap,String initKey) {
        if (StringUtils.isNotBlank(jsonLine)) {
            JSONObject json = JSONObject.parseObject(jsonLine);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //如果内层还是JSONObject的话,继续解析
                if (jsonMap.containsKey(k.toString())) {
                    if (StringUtils.isBlank(v.toString())){
                        jsonMap.put(initKey + "_" + k.toString(), "null");
                    }else {
                        jsonMap.put(initKey + "_" + k.toString(), v);
                    }
                } else {
                    if (StringUtils.isBlank(v.toString())) {
                        jsonMap.put(k.toString(), "null");
                    } else {
                        jsonMap.put(k.toString(), v);
                    }
                }
            }
        }
    }

    /**
     * 从外到内获取嵌套字段  同名key的情况重命名key（加上外部key.）
     * @param jsonMap
     */
    private static void getAnyInnerData(Map jsonMap){
        Set set = jsonMap.keySet();
        Object[] keyArray = set.toArray();
        for (int i = 0;i<keyArray.length;i++){
            String key = keyArray[i].toString();
            Object value = jsonMap.get(key);
            if (value instanceof JSONObject){
                jsonMap.remove(key);
                getInnerData2(value.toString(),jsonMap,key);
                getAnyInnerData(jsonMap);
            }
        }
    }
}
