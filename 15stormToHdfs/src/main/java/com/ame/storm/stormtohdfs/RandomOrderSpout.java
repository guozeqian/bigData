package com.ame.storm.stormtohdfs;

import com.ame.storm.orders.domain.PaymentInfo;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class RandomOrderSpout extends BaseRichSpout{

    private SpoutOutputCollector collector;

    /**
     * 初始化调用一次
     * @param conf
     * @param context
     * @param collector
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 反复的不断地额被调用
     */
    @Override
    public void nextTuple() {
        //获取一个json格式的订单字符串
        String random = new PaymentInfo().random();
        //发送数据的时候带了两个参数，第一个参数就是我们发送的数据，第二个参数带的是messageid
       //这里带上了msgId这个参数，就表明我们要启用ack机制
        collector.emit(new Values(random),random);
    }

    /**
     * 为我们发送出去的数据，申明一个变量，下游可以通过这个变量来进行取值
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("randomOrder"));

    }


    /**
     * 如果消息处理失败，会回调这个方法
     * @param msgId
     */
    @Override
    public void fail(Object msgId) {
      collector.emit(new Values(msgId));
    }



    /*
    如果消息处理成功会回调ack方法
     */

    @Override
    public void ack(Object msgId) {
        System.out.println("我处理成功了哈哈哈哈");
    }
}
