package com.jj.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * map与JSON之间的转换
 */
public class JsonAndMapConvertUtils {
    /**
     * map转换成json格式的字符串
     * @param map
     * @return
     */
    public static String mapToJSON(Map map){
        return JSON.toJSONString(map);
    }

    /**
     * json格式字符串转换成map集合
     * @param json
     * @return
     */
    public static Map JSONToMap(String json){
        return JSON.parseObject(json,Map.class);
    }

}
