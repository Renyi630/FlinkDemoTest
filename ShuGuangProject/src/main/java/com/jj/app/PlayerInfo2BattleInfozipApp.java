package com.jj.app;


import com.jj.filter.PlayerBattleFilter;
import com.jj.map.PlayInfo2BattleInfozipMap;
import com.jj.models.PlayInfoNeedzip;
import com.jj.sink.RedisSink;
import com.jj.utils.KafkaUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;

public class PlayerInfo2BattleInfozipApp {
    public static void main(String[] args) throws  Exception{

        ParameterTool configPath = ParameterTool.fromArgs(args);
        ParameterTool parameter;
        try {
            parameter = ParameterTool.fromPropertiesFile(configPath.get("conf"));
        } catch (IOException e) {
            parameter = ParameterTool.fromSystemProperties();
            e.printStackTrace();
        }
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        setConfig(env,parameter);


        DataStream<String> kafkaSource = KafkaUtils.createKafkaSource(env, parameter);
        kafkaSource
                .filter(new PlayerBattleFilter()).uid("PlayerBattleFilter").name("PlayerBattleFilter")
                .flatMap(new PlayInfo2BattleInfozipMap()).uid("PlayInfo2BattleInfozipMap").name("PlayInfo2BattleInfozipMap")
                .keyBy(PlayInfoNeedzip::getBattleid)
                .addSink(new RedisSink()).name("redisSink").uid("redisSink").setParallelism(6);
        env.execute();
    }


    private static void setConfig(StreamExecutionEnvironment env,ParameterTool parameter){

        env.enableCheckpointing(parameter.getInt("state.checkpoints.interval"));
        env.setParallelism(parameter.getInt("parallelism.default"));

        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,10000));
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        checkpointConfig.setMinPauseBetweenCheckpoints(parameter.getInt("state.checkpoints.pause"));
        checkpointConfig.setCheckpointTimeout(parameter.getInt("state.checkpoints.timeout"));

    }
}
