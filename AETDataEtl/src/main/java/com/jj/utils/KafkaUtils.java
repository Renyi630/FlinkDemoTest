package com.jj.utils;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;

import java.util.Properties;

public class KafkaUtils {

    public static DataStream<String> createKafkaSource(StreamExecutionEnvironment env, String broker,String topic,String groupid,String username,String password) {

        KafkaSource<String> kafkaSource = KafkaSource
                .<String>builder()
                .setBootstrapServers(broker)
                .setTopics(topic)
                .setGroupId(groupid)
//                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.LATEST))
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setProperty("security.protocol", "SASL_PLAINTEXT")
                .setProperty("sasl.mechanism", "PLAIN")
                .setProperty("sasl.jaas.config", String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",username,password))
                .build();

        return env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");
    }

    public static FlinkKafkaProducer<String> getKafkaProducer(String broker,String topic,String username,String password) {


        Properties props = new Properties();
        props.setProperty("bootstrap.servers", broker);
        props.setProperty(ProducerConfig.RETRIES_CONFIG, "5");
        //props.setProperty("transaction.timeout.ms",1000*60*10+"");
        props.setProperty(ProducerConfig.ACKS_CONFIG, -1 + "");
        //加上用户名和密码的验证
        String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required\nusername=\"%s\"\npassword=\"%s\";";
        String jaasConfig = String.format(jaasTemplate, username, password);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name);
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new FlinkKafkaProducer<String>(topic, new SimpleStringSchema(), props);
    }
}
