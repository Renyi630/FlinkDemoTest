package com.jj.map;

import com.jj.utils.TranjsonAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class AetStandardMap implements MapFunction<String, String> {
    String dataRegex;

    public AetStandardMap(String dataRegex) {
        this.dataRegex = dataRegex;
    }

    @Override
    public String map(String s) throws Exception {
        String result = "";
        if (StringUtils.isNotBlank(s)) {
            //主要业务逻辑解析
            result = TranjsonAnalysis.getFinalStandardData(s, dataRegex);
        }
        return result;
    }
}
