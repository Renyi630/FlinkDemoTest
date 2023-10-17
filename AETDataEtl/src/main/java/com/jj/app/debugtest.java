package com.jj.app;

import cn.thinkingdata.tga.javasdk.DebugConsumer;
import cn.thinkingdata.tga.javasdk.ThinkingDataAnalytics;

import java.net.URISyntaxException;
import java.util.*;

public class debugtest {
    public static void main(String[] args) throws URISyntaxException {
        ThinkingDataAnalytics.enableLog(true);

        /*
DebugConsumer:数据逐条上报。当出现问题时会以日志和异常的方式提示用户；不建议在线上环境使用
 */

        ThinkingDataAnalytics te = new ThinkingDataAnalytics(new DebugConsumer("http://10.11.199.162:8991","debug-appid","12345"));


        //设置事件属性
        Map<String,Object> properties = new HashMap<String,Object>();
// 设置用户的ip地址，TE系统会根据IP地址解析用户的地理位置信息
        properties.put("#ip", "192.168.1.1");//字符串
        properties.put("channel","te");//字符串
        properties.put("age",1);//数字

        properties.put("isSuccess",true);//布尔
        properties.put("birthday",new Date());//时间

        List<String>  arr1    = new ArrayList<String>();
        properties.put("arr",arr1);//数组

        try {
            te.track("account_id","distinct_id","payment",properties);
        } catch (Exception e) {
            System.out.println("except:"+e);
        }
    }
}
