package com.jj.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jj.utils.LogUtils;
import org.apache.flink.api.common.functions.FilterFunction;

public class PlayerBattleFilter implements FilterFunction<String> {
    @Override
    public boolean filter(String input) throws Exception {

        JSONObject jsonObject = JSON.parseObject(input);
        String op = jsonObject.getString("operation");

        return op.equals("PlayerBattle");
    }
}
