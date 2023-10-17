package com.jj.map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jj.models.PlayInfoNeedzip;
import com.jj.utils.LogUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class PlayInfo2BattleInfozipMap implements FlatMapFunction<String,PlayInfoNeedzip> {
    @Override
    public void flatMap(String input, Collector<PlayInfoNeedzip> ctx) throws Exception {
        PlayInfoNeedzip playInfoZip = null;
        try {
            JSONObject jsonObj = JSON.parseObject(input);
            playInfoZip = new PlayInfoNeedzip();
            playInfoZip.setBattleid(jsonObj.getLongValue("battleid"));
            playInfoZip.setHi(jsonObj.getIntValue("heroidx"));
            playInfoZip.setK(jsonObj.getIntValue("kill"));
            playInfoZip.setD(jsonObj.getIntValue("death"));
            playInfoZip.setA(jsonObj.getIntValue("assist"));
            playInfoZip.setMe(jsonObj.getIntValue("match_elo"));
            playInfoZip.setV(jsonObj.getIntValue("victory"));
            playInfoZip.setGt(jsonObj.getIntValue("gametype"));
            playInfoZip.setMt(jsonObj.getIntValue("modetid"));
            playInfoZip.setMtb(jsonObj.getIntValue("modetid_battle"));
            playInfoZip.setS(jsonObj.getDoubleValue("score"));
            playInfoZip.setDs(jsonObj.getDoubleValue("damagescore"));
            playInfoZip.setSs(jsonObj.getIntValue("survivalscore"));
            playInfoZip.setEs(jsonObj.getDoubleValue("economyscore"));
            playInfoZip.setTs(jsonObj.getIntValue("teamscore"));
            playInfoZip.setKs(jsonObj.getIntValue("kdascore"));
            playInfoZip.setDm(jsonObj.getIntValue("damage"));
            playInfoZip.setDmr(jsonObj.getIntValue("dmgreceived"));
            playInfoZip.setDmth(jsonObj.getIntValue("dmgtohero"));
            playInfoZip.setDmrh(jsonObj.getIntValue("dmgreceivedfromhero"));
            playInfoZip.setLt(jsonObj.getString("logtime"));
            playInfoZip.setPt(jsonObj.getIntValue("playtime"));
            playInfoZip.setBs(jsonObj.getIntValue("battle_sum"));
            playInfoZip.setL(jsonObj.getIntValue("level"));
            playInfoZip.setAt(jsonObj.getIntValue("ai_type"));
            playInfoZip.setAb(jsonObj.getIntValue("ai_base"));
        } catch (Exception e) {
            LogUtils.error("JSON parse error:"+input);
            playInfoZip=null;
        }
        if (playInfoZip!=null){
            ctx.collect(playInfoZip);
        }
    }
}
