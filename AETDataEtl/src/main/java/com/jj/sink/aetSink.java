package com.jj.sink;

import com.jj.utils.HttpUtils;
import com.jj.utils.LogUtils;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;

import java.util.List;

public class aetSink implements SinkFunction<String> {
    private String url = "http://10.11.190.66:80/sync_json";
    private final int batchSize = 100;
    private List<String> batchData=new ArrayList<String>();


    @Override
    public void invoke(String value, Context context) throws Exception {

        batchData.add(value);
        if (batchData.size() >= batchSize){
//            LogUtils.info(batchData.toString());
            long timestart = System.currentTimeMillis();
            HttpUtils.sendPostJsonBody(url,batchData.toString());
            long timeEnd = System.currentTimeMillis();
            System.out.println(timeEnd-timestart);
            batchData.clear();
        }
    }
}
