package com.ame.storm.logmonitor;

import com.ame.storm.logmonitor.utils.CommonUtils;
import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 这个是我们定时更新我们的数据库当中的规则到map当中来
 */
public class TickUpdateRuleBolt extends BaseBasicBolt{
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        config.put(config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,5);
        return config;
    }

    /**
     * 在这个bolt里面写一个定时器，定时的更新我们数据库当中的规则到map来存储

     */

    private  CommonUtils commonUtils;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        commonUtils = new CommonUtils();

    }

    /**
     * 判断究竟是系统发送的tuple还是上游发送的tuple
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        if(input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID) &&input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)){
            //这里就是我们的定时任务，每隔五秒钟要执行一次更新，更新我们数据库当中的规则到我们的map当中来存储

            //第一步：查询数据库

            //第二步：将数据库对应的数据关系保存到map当中去

            //查询出哪一个appId对应哪一个应用的对象
            commonUtils.monitorApp();
            //将我们的规则信息保存到map当中来，key是我们的appId，value就是一个set集合
            commonUtils.monitorRule();
            commonUtils.monitorUser();

        }else{
            //上游发送的tuple，我们需要获取数据，继续往下游发送
            //获取kafka当中过来的数据，下标是4
            String string = input.getString(4);
            collector.emit(new Values(string));

        }




    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("logMessage"));

    }
}
