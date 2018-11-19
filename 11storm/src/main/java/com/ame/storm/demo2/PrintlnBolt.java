package com.ame.storm.demo2;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class PrintlnBolt extends BaseBasicBolt {
    /**
     * 这个方法会反复不断的被调用，接收上游发送过来的tuple，
     * tuple里面就封装了我们的数据
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取下标是4的那个数据，就是我们kafka当中的数据
        Object value = input.getValue(4);
        System.out.println(value.toString());
       /* List<Object> values = input.getValues();
        System.out.println(values.toArray().toString());
        System.out.println(input.toString());*/
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
