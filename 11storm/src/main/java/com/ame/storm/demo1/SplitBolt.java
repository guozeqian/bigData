package com.ame.storm.demo1;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * bolt也需要继承一个类，表示我们是一个规范的bolt组件
 * bolt用于继承BaseBasicBolt
 */
public class SplitBolt extends BaseBasicBolt{
    /**
     * 这个方法也会反复不断的被调用，只要有上游发送的数据，这个方法就会执行
     *
     * @param tuple ：上游发送的数据，都包在这个tuple里面了，我们可以从tuple当中获取上游发送的数据
     * @param collector：往下游发送数据的
     */
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        //通过上游申明的字段来获取我们的数据的值
        Object hello = tuple.getValueByField("hello");
        if(null != hello  && hello.toString() != ""){
            //获取上游发送过来的字符串
            String line = hello.toString();//hello world
            //切割我们的字符串，然后继续往下游发送
            String[] split = line.split(" ");
            for (String word : split) {
                //word  1
                //hello  1
                Values values = new Values(word, 1);
                collector.emit(values);

            }
        }
    }

    /**
     * 还是给我们所有往下游发送的数据一个申明，下游可以通过这个申明获取到我们发送的数据
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word","nums"));
    }
}
