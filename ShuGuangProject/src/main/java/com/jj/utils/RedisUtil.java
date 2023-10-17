package com.jj.utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
public class RedisUtil {
    private static JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxWaitMillis(2000);
        poolConfig.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(poolConfig, "sgmai-001-redis.service.jjsrv.local", 9438);
    }

    public static String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("redis_sgmai001_JJMatch");
            return jedis.get(key);
        }
    }

    public static void set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("redis_sgmai001_JJMatch");
            jedis.set(key, value);
        }
    }

    public static void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("redis_sgmai001_JJMatch");
            jedis.del(key);
        }
    }

    /*
    rpush方法用于向list中传入一个string类型的值，如果list不存在则创建后传入，存在则添加至list尾部
     */
    public static void rpush(String key,String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("redis_sgmai001_JJMatch");
            jedis.rpush(key,value);
        }
    }

    public static void expire(String key,long expireSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("redis_sgmai001_JJMatch");
            jedis.expire(key,expireSeconds);
        }
    }
    public static void sadd(String key,String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("redis_sgmai001_JJMatch");
            jedis.sadd(key,value);
        }
    }
}
