package com.jj.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析通用json工具接口
 *
 * @author fyj
 * @version 1.1
 */
public class JsonParseDriver {
    /**
     * 解析json
     *
     * @param jsonLine  json字符串
     * @param lineRegex json字符串应满足的规则
     *
     * @return map集合
     */
    public static Map parseJson(String jsonLine, String lineRegex) {
        Map map = new HashMap();
        MatchJsonFormatUtils match = new MatchJsonFormatUtils();
        boolean flag = match.validate(jsonLine);
        if (flag) {
            boolean logFlag = jsonLine.matches(lineRegex);
            if (logFlag) {
                if (map == null) {
                    map = new HashMap();
                }
                ParseJson.getDetailData(jsonLine, map);
            }
        }
        return map;
    }

    /**
     * 解析json
     *
     * @param jsonLine json字符串
     *
     * @return
     */
    public static Map parseJson(String jsonLine) {
        Map map = new HashMap();
        MatchJsonFormatUtils match = new MatchJsonFormatUtils();
        boolean flag = match.validate(jsonLine);
        if (flag) {
            if (map == null) {
                map = new HashMap();
            }
            ParseJson.getDetailData(jsonLine, map);
        }
        return map;
    }
}
