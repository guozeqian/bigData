package com.ame.storm.ticktime;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

public class RandomSpout extends BaseRichSpout {

    private  SpoutOutputCollector collector;
    private String[] arrays ;
    private Random random;



    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        random = new Random();
        arrays = new String[]{"hello 12","tom 15","小明 18","林青霞 48"};

    }

    @Override
    public void nextTuple() {
        collector.emit(new Values(arrays[random.nextInt(arrays.length)]));


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("names"));

    }
}
