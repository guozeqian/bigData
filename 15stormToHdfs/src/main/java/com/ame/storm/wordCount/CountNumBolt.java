package com.ame.storm.wordCount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class CountNumBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        //处理逻辑
        try {
            String splitBoltWord = input.getStringByField("splitBoltWord");
            Thread.sleep(40000);
            collector.ack(input);
            //如果处理失败，记得要调用fail方法
         //   collector.fail(input);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
