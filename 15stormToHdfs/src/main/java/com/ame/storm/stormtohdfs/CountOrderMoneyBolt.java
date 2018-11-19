package com.ame.storm.stormtohdfs;

import com.ame.storm.orders.domain.PaymentInfo;
import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CountOrderMoneyBolt  extends BaseRichBolt{

    //  OOM  OUT  OF  MEMORY  EXCEPTION    ACk机制

    private  OutputCollector collector;

    private  static ConcurrentHashMap<String,Long> map = new ConcurrentHashMap<String,Long>();


    /**
     * 初始化调用一次
     * @param stormConf
     * @param context
     * @param collector
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 这个方法会被反复的不断的调用
     * @param input
     */
    @Override
    public void execute(Tuple input) {
        String randomOrder = input.getStringByField("randomOrder");
        JSONObject jsonObject = new JSONObject();
        //将我们上游发送过来的订单的字符串转换成对象，然后对金额进行累加
        PaymentInfo paymentInfo = jsonObject.parseObject(randomOrder, PaymentInfo.class);
        if(map.containsKey("orderMoney")){
            map.put("orderMoney",map.get("orderMoney")+paymentInfo.getPayPrice());
        }else{
            map.put("orderMoney",paymentInfo.getPayPrice());
        }

        System.out.println(map.toString());
        collector.emit(new Values(randomOrder));
        //发送消息处理成功的状态到内存池当中去，可以通过异或算法，清除掉内存池当中的数据

        collector.ack(input);
        //处理失败
       /// collector.fail(input);



    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("countMoneyBolt"));

    }





}
