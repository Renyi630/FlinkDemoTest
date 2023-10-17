package com.jj.sink;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jj.models.PlayInfoNeed;
import com.jj.models.PlayInfoNeedzip;
import com.jj.utils.LogUtils;
import com.jj.utils.RedisUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/*
    需把redis的key：battle_id先取出来，再把数据中key删掉，value写入redis
 */
public class RedisSink implements SinkFunction<PlayInfoNeedzip> {
    long expireSeconds = 7 * 24 * 60 * 60;

    @Override
    public void invoke(PlayInfoNeedzip playInfoNeed, Context context) throws Exception {
        long battleId = playInfoNeed.getBattleid();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(playInfoNeed));
        jsonObject.remove("battleid");
        String BattleInfo = jsonObject.toJSONString();
        RedisUtil.sadd(String.valueOf(battleId),BattleInfo);
        RedisUtil.expire(String.valueOf(battleId),expireSeconds);
//        LogUtils.info(BattleInfo);
    }
}
