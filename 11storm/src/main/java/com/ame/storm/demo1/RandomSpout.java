package com.ame.storm.demo1;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * 为了符合我们的spout的规范，我们需要继承BaseRichSpout
 */
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private  String[]  arrays;
    private Random random;
    /**
     * 初始化的方法，程序启动的时候，首先调用一次
     * @param conf
     * @param context
     * @param collector
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        random = new Random();
        arrays = new String[]{"hello world","hadoop hive","hello kitty","sqoop hadoop"};
        this.collector = collector;
    }
    /**
     * 这个方法会反复不断的一直被执行，在这个方法里面，将我们的数据往下游发送，通过 SpoutOutputCollector
     * 将我们的数据往下游发送
     */
    @Override
    public void nextTuple() {

        try {
            //通过调用emit方法，将我们的数据往外发送，需要接受一个list类型的参数
            //values继承了arrayList   就是一个list
            //这里面包的就是我们需要往下游发送的数据r
            int i = random.nextInt(arrays.length);
            String line = arrays[i];
            Thread.sleep(1000);
            Values hello = new Values(line);
            collector.emit(hello);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    /**
     * 为我们往下游发送的单词申明一个字符串
     * 下游获取单词的时候，可以直接通过这个字符串获取
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("hello"));

    }
}
