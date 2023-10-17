package com.jj.map;

import com.alibaba.fastjson2.JSONObject;
import com.jj.common.TDFormat;
import org.apache.flink.api.common.functions.FlatMapFunction;

import org.apache.flink.util.Collector;

import java.util.List;

public class AetListFlatMap implements FlatMapFunction<List<JSONObject>,String> {

    @Override
    public void flatMap(List<JSONObject> input, Collector<String> out) throws Exception {
        for (JSONObject jsonObject : input) {
            String jsonString = TDFormat.TDAetFormatter(jsonObject);
            System.out.println(jsonString);
            out.collect(jsonString);
        }
    }
}
