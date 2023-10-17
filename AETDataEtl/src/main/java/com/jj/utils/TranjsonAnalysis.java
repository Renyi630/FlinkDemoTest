package com.jj.utils;

import com.alibaba.fastjson.JSONException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tiangx
 */
public class TranjsonAnalysis {
    /**
     * 对格式化的数据进行二次加工  时间格式化
     */
    public static String getFinalStandardData(String line, String dataRegex)
            throws IndexOutOfBoundsException, JSONException, NullPointerException {

        String[] lineArray = line.split(",", -1);

        Map map = new LinkedHashMap();//记录公共信息
        if (lineArray.length >= 7) {
            String rcvtime = lineArray[0];
            String appname = lineArray[1];
            String ostype = lineArray[2];
            String version = lineArray[3];
            String iscompress = lineArray[4];
            String md5 = lineArray[5];
            map.put("rcvtime", rcvtime);
            map.put("appname", appname);
            map.put("ostype", ostype);
            map.put("version", version);
            map.put("iscompress", iscompress);
            map.put("md5", md5);
        }
        //匹配HardWare替换 ("HardWare":")(.*?)(",) 临时处理无法解密数据
//        Pattern hpattern = Pattern.compile("(\"HardWare\":\")(.*?)(\",)");
//        Matcher hmatcher = hpattern.matcher(line);
//        String line2 = hmatcher.replaceAll("\"HardWare\":\"\",");

        //匹配json数据
        Pattern pattern = Pattern.compile("(\\{)(.*)(\\})");
        Matcher matcher = pattern.matcher(line);
//        if (line.contains("RealIMEI")){
        if (matcher.find()) {
            String jsonData = matcher.group(0);
//            System.out.println(jsonData);
            if (jsonData.matches(dataRegex)) {
                jsonData = jsonData.replace("\\,", ",");
                Map userActMap = JsonParseDriver.parseJson(jsonData, dataRegex);
                if (userActMap != null && userActMap.size() != 0) {
                    userActMap.putAll(map);
                    String resultJson = JsonAndMapConvertUtils.mapToJSON(userActMap);
                    if (null != resultJson) {
                        return resultJson;//.toLowerCase();
                    }
                }
            }
        } else {
            System.out.println("无法匹配数据：" + line);
        }
//        }else {
//            System.out.println("加密数据无法处理：" + line);
//        }

        return "";


    }

    /**
     * 对格式化的Msg数据进行二次加工  时间格式化
     */
    public static String getMsgStandardData(String line, String dataRegex)
            throws IndexOutOfBoundsException, JSONException, NullPointerException {
        try {
            Map map = new LinkedHashMap();//记录公共信息

            //匹配json数据
            Pattern pattern = Pattern.compile("(\\{)(.*)(\\})");
            Matcher matcher = pattern.matcher(line);
//        if (line.contains("RealIMEI")){
            if (matcher.find()) {
                String jsonData = matcher.group(0);
                if (jsonData.matches(dataRegex)) {
                    jsonData = jsonData.replace("\\,", ",");
                    Map userActMap = JsonParseDriver.parseJson(jsonData, dataRegex);
                    if (userActMap != null && userActMap.size() != 0) {
                        userActMap.putAll(map);
                        String resultJson = JsonAndMapConvertUtils.mapToJSON(userActMap);
                        HashMap mapJson = (HashMap) JsonAndMapConvertUtils.JSONToMap(resultJson);
                        if (mapJson.containsKey("Pages")) {
                            List list = (List) mapJson.get("Pages");
                            List<String> msgs = Lists.newArrayList();
                            for (Object j : list) {
                                HashMap page_map = (HashMap) JsonAndMapConvertUtils.JSONToMap(j.toString());
                                if (page_map.containsKey("extraMsg")) {
                                    msgs.add(page_map.get("extraMsg").toString());
                                }
                                if (page_map.containsKey("commonData")) {
                                    msgs.add(page_map.get("commonData").toString());
                                }
                            }
                            return Joiner.on("\n").skipNulls().join(msgs);
                        }
                        return "";
                    }
                }
            } else {
                System.out.println("无法匹配数据：" + line);
            }
//        }else {
//            System.out.println("加密数据无法处理：" + line);
//        }
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        return "";


    }

    /**
     * 对格式化的event_data
     */
    public static String getEventStandardData(String line, String dataRegex)
            throws IndexOutOfBoundsException, JSONException, NullPointerException {
        try {
            Map map = new LinkedHashMap();//记录公共信息

            //匹配json数据
            Pattern pattern = Pattern.compile("(\\{)(.*)(\\})");
            Matcher matcher = pattern.matcher(line);
//        if (line.contains("RealIMEI")){
            if (matcher.find()) {
                String jsonData = matcher.group(0);
                if (jsonData.matches(dataRegex)) {
                    jsonData = jsonData.replace("\\,", ",");
                    Map userActMap = JsonParseDriver.parseJson(jsonData, dataRegex);
                    if (userActMap != null && userActMap.size() != 0) {
                        userActMap.putAll(map);
                        String resultJson = JsonAndMapConvertUtils.mapToJSON(userActMap);
                        HashMap mapJson = (HashMap) JsonAndMapConvertUtils.JSONToMap(resultJson);
                        if (mapJson.containsKey("Pages")) {
                            List list = (List) mapJson.get("Pages");
                            List<String> msgs = Lists.newArrayList();
                            for (Object j : list) {
                                HashMap page_map = (HashMap) JsonAndMapConvertUtils.JSONToMap(j.toString());
                                if (page_map.containsKey("extraMsg")) {
                                    msgs.add(page_map.get("extraMsg").toString());
                                }
                                if (page_map.containsKey("commonData")) {
                                    msgs.add(page_map.get("commonData").toString());
                                }
                            }
                            return Joiner.on("\n").skipNulls().join(msgs);
                        }
                        return "";
                    }
                }
            } else {
                System.out.println("无法匹配数据：" + line);
            }
//        }else {
//            System.out.println("加密数据无法处理：" + line);
//        }
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        return "";


    }

    //加载配置文件
    public static Properties prop = null;
    static {
        InputStream in = null;
        try {
            in = TranjsonAnalysis.class.getClassLoader().getResourceAsStream("conf.properties");
            prop = new Properties();
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public final static String ORIGINAL_DATA_REGEX = "original.data.regex";
    public  static  void main(String[] args){
        //获取动态配置参数
//        final String standardRegex = prop.getProperty(STANDARD_OUT_REGEX);
        final String dataRegex = prop.getProperty(ORIGINAL_DATA_REGEX);
//        final String pageids = prop.getProperty(PAGE_ID_S);

        String line="1543075200,jjmatch,android,3,0,f9752428acc35115e9e8ff7154a4d05c,15430752001935456d176bfca039c7c7,{\"InstallGames\":[[\"2\",\"16\",\"17\",\"15\",\"2009\",\"600019\",\"11\",\"1017\",\"1027\",\"1007\",\"1056\",\"1031\",\"1076\",\"610002\",\"610001\",\"1022\",\"611000\"]],\"Users\":[{\"UserId\":442489030,\"STime\":9883,\"UserType\":2}],\"AppID\":2,\"appname\":\"jjmatch\",\"Packages\":[{\"ETime\":10412,\"packageId\":\"15\",\"STime\":10245,\"type\":\"install\",\"status\":\"install_succ\"}],\"rcvtime\":\"1543420820\",\"RLTime\":9883,\"OS\":\"android\",\"NetWork\":[{\"LocalIp\":\"\",\"NetCarrier\":\"\",\"ServerIp\":\"115.182.2.50\",\"STime\":9673,\"NetType\":\"WIFI\"}],\"StartTime\":1543420584924,\"iscompress\":\"1\",\"SDKVer\":3321,\"HardWare\":\"�U?c#O@H\\u001C\\u0016%�\\u0001.F(]\\u000E�摰�ŀ~��5\\u0003&\\u001D��&�n\\u0007�\\u0000o��r\\u0012)\\u000B�n�\\u00116/-$)h�k�`�\\u001E+��ȿ\\f4\\\\ֺ;�I>�ڸ�\\u00130\\u0002�\\\\\\u0013�\\u0001g2�u�l\\u0006�\\u0002��ʒڥ\\\"��\\b��\\u001E��z;a�}�nN��ܱY)8^��\\u0011\\u001F\\u001F�\\u0000�\\u00026r^�Q�u�:\\u0013\\u001B��&�f�Hv4�����\\u0016\\u0014o�UÏZ�adڒ�B&���=4���L���PE!pxgò#iv��Cv���\\u0011{�\\u007F�j���?n)��kͻ��*j��\\t\\\"\\u007F`�\uEE89\\u0004у�&{�(c5U�\\u0013&\\u001C�,Y'�il4V\\t��Xl%54|�@��WFf���\\u000E�NOM*S�\\u0004ZB-B]�����-N\\u001E��\\u000E*�4�3p\\u007F��m�W\\u0000�z�a\\t��[5*���G\\u0014����t�;5D�Jȅ��v��\uE043��k&ܔؒ�M5�`�\\u001DSǃ\\u0019\\u000F��t}��\\\"H��� �{��\\bf\\u001D\\u0013�sD��_�S�\\\"!I�)�N��b�ߨx$]��jk\\u0006�pjO��rFĺW+�7��M%��Y�]\\u0016\\u001E-\\b���L`'�^\\u0000�\\u000B�-�a<�}� \\u007F�\\f-b��W��5\\u001E-�\\bz\\u000E��\\\"��\\u00159�\\u007F�\",\"AppVer\":50505,\"ostype\":\"android\",\"ChannelID\":52560,\"TrackVer\":3,\"ServerTime\":1543420591000,\"version\":\"3\",\"Pages\":[{\"PageID\":\"1\",\"PkgVer\":3321,\"ETime\":299,\"STime\":0,\"PkgID\":1,\"GameID\":1},{\"PageID\":\"20001\",\"PkgVer\":3321,\"ETime\":23157,\"STime\":7390,\"PkgID\":1,\"GameID\":1},{\"PageID\":\"4\",\"PkgVer\":32072713,\"ETime\":60656,\"STime\":23338,\"PkgID\":2,\"GameID\":1},{\"PageID\":\"5\",\"PkgVer\":32802902,\"ETime\":124021,\"STime\":60876,\"PkgID\":2,\"GameID\":2},{\"PageID\":\"5\",\"PkgVer\":32802902,\"CurViewName\":\"match_select_view_1001\",\"ETime\":74132,\"STime\":66463,\"PkgID\":2,\"GameID\":2},{\"PageID\":\"5\",\"PkgVer\":32802902,\"CurViewName\":\"signUpDlg\",\"productId\":995,\"ETime\":116257,\"STime\":114331,\"PkgID\":2,\"GameID\":1001},{\"PageID\":\"20001\",\"PkgVer\":3321,\"ETime\":128126,\"STime\":124174,\"PkgID\":1,\"GameID\":1},{\"PageID\":\"4\",\"PkgVer\":30530415,\"ETime\":136109,\"STime\":128192,\"PkgID\":1027,\"GameID\":1},{\"PageID\":\"5\",\"PkgVer\":33080501,\"ETime\":145007,\"STime\":136283,\"PkgID\":1027,\"GameID\":1027},{\"PageID\":\"5\",\"PkgVer\":33080501,\"CurViewName\":\"signUpDlg\",\"productId\":9752,\"STime\":143071,\"PkgID\":1027,\"GameID\":1027},{\"PageID\":\"100\",\"PkgVer\":33080501,\"STime\":145628,\"PkgID\":1027,\"GameID\":1027},{\"PageID\":\"100\",\"PkgVer\":33080501,\"CurViewName\":\"ChasePigPlayView\",\"STime\":148588,\"PkgID\":1027,\"GameID\":1027},{\"PageID\":\"100\",\"PkgVer\":33080501,\"STime\":148691,\"extraMsg\":{\"gameid\":1027,\"tourneyid\":1406950,\"time\":1543420729814,\"exp\":41488,\"type\":\"playtimes\",\"userid\":442489030,\"matchid\":459227312},\"PkgID\":1027,\"GameID\":1027}],\"Games\":[[{\"gameId\":\"2\",\"posId\":\"6\"},{\"gameId\":\"16\",\"posId\":\"317\"},{\"gameId\":\"17\",\"posId\":\"344\"},{\"gameId\":\"15\",\"posId\":\"312\",\"childrenData\":[{\"gameId\":\"2009\",\"posId\":\"143\"},{\"gameId\":\"2014\",\"posId\":\"259\"}]},{\"gameId\":\"600019\",\"posId\":\"252\"},{\"gameId\":\"21\",\"posId\":\"454\"},{\"gameId\":\"2010\",\"posId\":\"245\"},{\"gameId\":\"620042\",\"posId\":\"424\"},{\"gameId\":\"0\",\"posId\":\"68\",\"childrenData\":[{\"gameId\":\"11\",\"posId\":\"290\"},{\"gameId\":\"1042\",\"posId\":\"355\"},{\"gameId\":\"1017\",\"posId\":\"356\"},{\"gameId\":\"1082\",\"posId\":\"401\"},{\"gameId\":\"1079\",\"posId\":\"417\"}]},{\"gameId\":\"0\",\"posId\":\"176\",\"childrenData\":[{\"gameId\":\"1006\",\"posId\":\"164\"},{\"gameId\":\"1027\",\"posId\":\"167\"},{\"gameId\":\"1035\",\"posId\":\"348\"},{\"gameId\":\"1007\",\"posId\":\"124\"},{\"gameId\":\"7\",\"posId\":\"78\"},{\"gameId\":\"1056\",\"posId\":\"197\"}]},{\"gameId\":\"0\",\"posId\":\"169\",\"childrenData\":[{\"gameId\":\"2017\",\"posId\":\"314\"},{\"gameId\":\"2022\",\"posId\":\"365\"},{\"gameId\":\"2025\",\"posId\":\"471\"}]},{\"gameId\":\"0\",\"posId\":\"443\",\"childrenData\":[{\"gameId\":\"620017\",\"posId\":491},{\"gameId\":\"620032\",\"posId\":\"368\"},{\"gameId\":\"620052\",\"posId\":\"476\"},{\"gameId\":\"620029\",\"posId\":490},{\"gameId\":\"620008\",\"posId\":\"199\"}]},{\"gameId\":\"620018\",\"posId\":\"342\"},{\"gameId\":\"500000\",\"posId\":\"431\"},{\"gameId\":\"1031\",\"posId\":\"181\"},{\"gameId\":\"1076\",\"posId\":\"336\"},{\"gameId\":\"620029\",\"posId\":\"361\"},{\"gameId\":\"620052\",\"posId\":\"475\"},{\"gameId\":\"620017\",\"posId\":\"267\"},{\"gameId\":\"610002\",\"posId\":\"390\"},{\"gameId\":\"610001\",\"posId\":\"183\"},{\"gameId\":\"0\",\"posId\":\"400\",\"childrenData\":[{\"gameId\":\"1022\",\"posId\":\"185\"},{\"gameId\":\"1083\",\"posId\":\"423\"},{\"gameId\":\"1070\",\"posId\":\"364\"}]},{\"gameId\":\"0\",\"posId\":\"387\",\"childrenData\":[{\"gameId\":\"620045\",\"posId\":\"464\"},{\"gameId\":\"620039\",\"posId\":\"413\"},{\"gameId\":\"620043\",\"posId\":\"445\"},{\"gameId\":\"611000\",\"posId\":\"452\"}]}]],\"STime\":0,\"SessionID\":\"9e0d349b3194a70fdff217607d72224d\",\"CPromoter\":52560,\"md5\":\"77701b53418478341af8b6d151e1e213\"}";
        String jsonData = getFinalStandardData(line,dataRegex);
        System.out.println(jsonData);
//        String jsonData="{\"OS\":\"android\",\"Pages\":[{\"STime\":0,\"PkgID\":1,\"PageID\":\"1\",\"ETime\":505,\"GameID\":1,\"PkgVer\":3124138},{\"STime\":5016,\"PkgID\":1,\"PageID\":\"95\",\"ETime\":7605,\"GameID\":1,\"PkgVer\":3124138},{\"STime\":8156,\"PkgID\":1,\"PageID\":\"100001\",\"ETime\":22980,\"GameID\":1,\"PkgVer\":3124138},{\"STime\":10042,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"project\":\"jjlobby\",\"refresh\":\"custom\",\"matchs\":{},\"games\":[\"2\",\"15\",\"16\",\"2018\"],\"rulesType\":0,\"event_name\":\"mygame_exposure\",\"exposureType\":\"listB\",\"event_time\":1592813987207},\"GameID\":1,\"PkgVer\":3124138},{\"PkgID\":1,\"STime\":13841,\"ETime\":15601,\"GameID\":1,\"productId\":3363,\"PageID\":\"100001\",\"CurViewName\":\"signUpDlg\",\"PkgVer\":3124138},{\"STime\":16803,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"project\":\"jjlobby\",\"refresh\":\"custom\",\"matchs\":{},\"games\":[\"2\",\"16\",\"27\",\"15\",\"600019\",\"1031\",\"1006\",\"0\",\"0\",\"21\",\"1076\",\"0\"],\"rulesType\":0,\"event_name\":\"mygame_exposure\",\"exposureType\":\"listA1\",\"event_time\":1592813993969},\"GameID\":1,\"PkgVer\":3124138},{\"STime\":19409,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"matchs\":{},\"games\":[\"2\",\"16\",\"27\",\"15\",\"600019\",\"1031\",\"1006\",\"0\",\"0\",\"21\",\"1076\",\"0\"],\"exposureType\":\"listA1\",\"project\":\"jjlobby\",\"event_name\":\"mygame_exposure_click\",\"position\":2,\"rulesType\":0,\"click_type\":\"game\",\"click\":\"16\",\"event_time\":1592813996575},\"GameID\":1,\"PkgVer\":3124138},{\"STime\":22782,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"matchs\":{},\"games\":[\"2\",\"16\",\"27\",\"15\",\"600019\",\"1031\",\"1006\",\"0\",\"0\",\"21\",\"1076\",\"0\"],\"exposureType\":\"listA1\",\"project\":\"jjlobby\",\"event_name\":\"mygame_exposure_click\",\"position\":1,\"rulesType\":0,\"click_type\":\"game\",\"click\":\"2\",\"event_time\":1592813999943},\"GameID\":1,\"PkgVer\":3124138},{\"STime\":23148,\"PkgID\":2,\"PageID\":\"4\",\"ETime\":28020,\"GameID\":1,\"PkgVer\":40134304},{\"STime\":28106,\"PkgID\":1,\"PageID\":\"100001\",\"ETime\":32990,\"GameID\":1,\"PkgVer\":3124138},{\"STime\":28711,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"project\":\"jjlobby\",\"refresh\":\"custom\",\"matchs\":{},\"games\":[\"2\",\"16\",\"27\",\"15\",\"600019\",\"1031\",\"1006\",\"0\",\"0\",\"21\",\"1076\",\"0\"],\"rulesType\":0,\"event_name\":\"mygame_exposure\",\"exposureType\":\"listA1\",\"event_time\":1592814005877},\"GameID\":1,\"PkgVer\":3124138},{\"STime\":29959,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"project\":\"jjlobby\",\"refresh\":\"custom\",\"matchs\":[\"1051_3360\",\"1051_3363\",\"1080_3241\",\"1001_994\"],\"games\":[\"2\",\"15\",\"16\",\"2018\"],\"rulesType\":0,\"event_name\":\"mygame_exposure\",\"exposureType\":\"listB\",\"event_time\":1592814007122},\"GameID\":1,\"PkgVer\":3124138},{\"actionName\":\"slideToMyGame\",\"STime\":29965,\"PkgID\":1,\"PageID\":\"100001\",\"GameID\":1,\"PkgVer\":3124138},{\"STime\":32788,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"matchs\":[\"1051_3360\",\"1051_3363\",\"1080_3241\",\"1001_994\"],\"games\":[\"2\",\"15\",\"16\",\"2018\"],\"exposureType\":\"listB\",\"project\":\"jjlobby\",\"event_name\":\"mygame_exposure_click\",\"position\":2,\"rulesType\":0,\"click_type\":\"game\",\"click\":\"15\",\"event_time\":1592814009950},\"GameID\":1,\"PkgVer\":3124138},{\"STime\":33017,\"PkgID\":15,\"PageID\":\"72\",\"ETime\":35739,\"GameID\":15,\"PkgVer\":38830029},{\"STime\":35816,\"PkgID\":1,\"PageID\":\"100001\",\"GameID\":1,\"PkgVer\":3124138},{\"STime\":36300,\"PkgID\":1,\"PageID\":\"100001\",\"extraMsg\":{\"project\":\"jjlobby\",\"refresh\":\"custom\",\"matchs\":[\"1051_3360\",\"1051_3363\",\"1080_3241\",\"1001_994\"],\"games\":[\"2\",\"15\",\"16\",\"2018\"],\"rulesType\":0,\"event_name\":\"mygame_exposure\",\"exposureType\":\"listB\",\"event_time\":1592814013466},\"GameID\":1,\"PkgVer\":3124138},{\"PkgID\":1,\"STime\":38765,\"ETime\":40044,\"GameID\":1,\"productId\":994,\"PageID\":\"100001\",\"CurViewName\":\"signUpDlg\",\"PkgVer\":3124138}],\"SDKVer\":3124138,\"Session\":{\"SessionID\":\"a56cb77a87bed1bcd9299bd45817ce33\",\"STime\":0},\"AppVer\":50909,\"Users\":[{\"STime\":6814,\"UserId\":716840134,\"UserType\":10}],\"HardWare\":{\"RealMac\":\"\",\"ScreenSize\":\"720x1439\",\"ICCID\":\"\",\"DeviceModel\":\"CLT-AL00\",\"SiteId\":99999,\"StorageSize\":{\"originalphonesize\":120584142848,\"SDsize\":80299937792,\"phonesize\":80320909312,\"originalSDsize\":120563171328},\"IMEI\":\"26D9E9499CBBD41\",\"SysVer\":\"10\",\"MID\":\"\",\"NetType\":2,\"Mac\":\"E4:0E:EE:FD:4F:E3\",\"NetName\":\"4G\",\"Processor\":\"\",\"Operator\":\"Telcom\",\"RealIMEI\":\"\",\"IMSI\":\"\"},\"Packages\":[{\"STime\":19723,\"size\":55080557,\"type\":\"download\",\"lastVer\":0,\"newVer\":\"40900819\",\"packageId\":\"16\"}],\"Games\":{},\"AppID\":2,\"CPromoter\":99999,\"NetWork\":[{\"NetType\":\"4G\",\"NetCarrier\":\"Telcom\",\"LocalIp\":\"42.249.51.216\",\"STime\":5594,\"ServerIp\":\"115.182.2.33\"}],\"InstallGames\":[[\"2\",\"15\",\"610001\",\"610002\",\"620063\",\"620067\",\"611003\",\"611000\",\"611001\",\"610003\"]],\"ChannelID\":99999,\"Time\":{\"StartTime\":1592813981645,\"ServerTime\":1592813983000,\"RLTime\":5844},\"TrackVer\":3}";
//        System.out.println(getEventStandardData(jsonData,dataRegex));

    }
}
