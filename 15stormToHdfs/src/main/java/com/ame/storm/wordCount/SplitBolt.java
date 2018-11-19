package com.ame.storm.wordCount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class SplitBolt extends BaseRichBolt {

    private  OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector =collector;
    }

    @Override
    public void execute(Tuple input) {

        String randomStr = input.getStringByField("randomStr");
        //注意这里带上了两个参数表示，我们数据是从哪个tuple过来的
        collector.emit(input,new Values(randomStr));
        //确认我们的消息已经收到了
        collector.ack(input);


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("splitBoltWord"));

    }
}
