package com.ame.storm.wordCount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

public class RandomWordSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private Random random;
    private String[] arrays;


    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        random = new Random();
        arrays = new String[]{"hello world","hadoop hive","flume hadoop"};
    }

    @Override
    public void nextTuple() {
        String line = arrays[random.nextInt(arrays.length)];
        //带上我们的msgId表示我们开启消息ack确认机制
        collector.emit(new Values(line),line);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("randomStr"));

    }

    @Override
    public void ack(Object msgId) {
        System.out.println("我是成功回调的方法，哈哈哈哈");
    }

    @Override
    public void fail(Object msgId) {
        //如果数据处理失败，那么就回调这个fail方法，我们可以将我们的数据重新发送
        collector.emit(new Values(msgId),msgId);
        System.out.println("我是失败回调的方法，呜呜呜呜"+msgId.toString());
    }
}
