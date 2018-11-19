package com.ame.storm.orders.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {


    private static JedisPool pool = null;

    /**
     * 获取jedis连接池
     * */
    public static JedisPool getPool(){
        if(pool == null){
            //创建jedis连接池配置
            JedisPoolConfig config = new JedisPoolConfig();
            //最大连接数
            config.setMaxTotal(20);
            //最大空闲连接
            config.setMaxIdle(5);
            //创建redis连接池
            pool = new JedisPool(config,"192.168.52.202",6379,3000);
        }
        return pool;
    }

    /**
     * 获取jedis连接
     * */
    public static Jedis getConn(){
        return getPool().getResource();
    }


    public static void main(String[] args) {
        Jedis jedis = getPool().getResource();
        jedis.incrBy("mine", 5);
        jedis.close();
    }


}
