package com.ame.readboard.storm;

import com.ame.readboard.domain.PaymentInfo;
import com.ame.readboard.utils.JedisUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class CountMoneyBolt extends BaseBasicBolt {
    private  JSONObject jsonObject;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        jsonObject = new JSONObject();

    }

    /**
     * 接收我们上游kafkaspout发送过来的数据，然后将数据保存到redis当中去
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //上游发送过来定的数据都是一个个的json格式的字符串
        Object value = input.getValue(4);
        if(null != value && value.toString() != ""){
            //获取我们json格式的字符串
            String jsonStr = value.toString();
            //将我们的json转换成对象
         //   JSONObject jsonObject = new JSONObject();
            PaymentInfo paymentInfo = jsonObject.parseObject(jsonStr, PaymentInfo.class);

            //获取redis的客户端
            Jedis conn = JedisUtil.getConn();
            // 平台总销售额度
            conn.incrBy("itcast:order:total:price:date",paymentInfo.getPayPrice());
            //平台今天下单的人数
            conn.incr("itcast:order:total:user:date");
            //平台销售的商品数量
            conn.incr("itcast:order:num:user:date");

            //每个商品的总销售额
            conn.incrBy("itcast:order:"+paymentInfo.getProductId()+":price:date",paymentInfo.getPayPrice());
            //统计每个商品的购买人数
            conn.incr("itcast:order:"+paymentInfo.getProductId()+":user:date");
            // 每个商品的销售数量
            conn.incr("itcast:order:"+paymentInfo.getProductId()+":num:date");


            //店铺的销售总额
            conn.incrBy("itcast:order:"+paymentInfo.getShopId()+":price:date",paymentInfo.getPayPrice());
//店铺的购买人数
            conn.incr("itcast:order:"+paymentInfo.getShopId()+":user:date");
            //每个店铺的销售数量
            conn.incr("itcast:order:"+paymentInfo.getShopId()+":num:date");
            conn.close();

            /**
             *   每个店铺的总销售额
             Redis的rowKey设计itcast:order:shopId:price:date
             每个店铺的购买人数
             Redis的rowKey设计itcast:order:shopId:user:date
             每个店铺的销售数量
             Redis的rowKey设计itcast:order:shopId:num:date
             */


            /**
             *
             平台总销售额度
             redisRowKey设计  itcast:order:total:price:date
             平台今天下单人数
             redisRowKey设计  itcast:order:total:user:date
             平台商品销售数量
             redisRowKey设计  itcast:order:num:user:date

             每个商品的总销售额
             Redis的rowKey设计itcast:order:productId:price:date
             每个商品的购买人数
             Redis的rowKey设计itcast:order:productId:user:date
             每个商品的销售数量
             Redis的rowKey设计itcast:order:productId:num:date

             每个店铺的总销售额
             Redis的rowKey设计itcast:order:shopId:price:date
             每个店铺的购买人数
             Redis的rowKey设计itcast:order:shopId:user:date
             每个店铺的销售数量
             Redis的rowKey设计itcast:order:shopId:num:date
             */


        }


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
