package com.jj.app;


import com.alibaba.fastjson2.JSONObject;
import com.jj.common.TDFormat;
import com.jj.map.AetFlatMap;
import com.jj.map.AetMsgStandardMap;
import com.jj.map.AetStandardMap;
import com.jj.map.JsonFormatMap;
import com.jj.sink.aetSink;
import com.jj.utils.KafkaUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.IOException;


public class AETDataEtlApp {
    public static void main(String[] args) throws Exception {
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
        env.setParallelism(4);
        env.disableOperatorChaining();


        DataStream<String> kafkaSource = KafkaUtils.createKafkaSource(env, parameter.get("aet.broker"),parameter.get("aet.source.topic"),parameter.get("aet.source.groupid"),parameter.get("aet.userid"),parameter.get("aet.password"));

        String dataRegex = parameter.get("original.data.regex");

        SingleOutputStreamOperator<JSONObject> map = kafkaSource.map(new JsonFormatMap()).uid("JsonFormatMap").name("JsonFormatMap");

//        event
        map.filter(jsonObject -> { return jsonObject.getString("event_name")!=null;}).uid("eventFilter").name("eventFilter")
                        .map(new MapFunction<JSONObject, String>() {
                            @Override
                            public String map(JSONObject jsonObject) throws Exception {
                                String s = TDFormat.TDEventFormatter(jsonObject);
                                return s;
                            }
                        }).uid("TDFormatMap").name("TDFormatMap")
                .addSink(new aetSink()).uid("EventSink").name("EventSink").setParallelism(10);


//                .addSink(KafkaUtils.getKafkaProducer(parameter.get("aet.broker"),parameter.get("aet.sink.topic"),parameter.get("aet.userid"),parameter.get("aet.password"))).uid("Eventsink");



        //aet
        SingleOutputStreamOperator<String> aetStream = kafkaSource
                .filter(s -> {
                    return s != null && !s.equals("") ;
                }).uid("空值filter").name("空值filter");
        aetStream
                .map(new AetStandardMap(dataRegex)).uid("AetStandardMap").name("AetStandardMap")
                .flatMap(new AetFlatMap())
                .addSink(new aetSink()).uid("AetSink").name("AetSink").setParallelism(10);

        aetStream
                .filter(s -> s.contains("extraMsg") || s.contains("commonData"))
                .map(new AetMsgStandardMap(dataRegex))
                .flatMap(new AetFlatMap());


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
