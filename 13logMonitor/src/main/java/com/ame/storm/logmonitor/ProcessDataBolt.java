package com.ame.storm.logmonitor;

import com.ame.storm.logmonitor.utils.CommonUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * 数据解析的bolt，确认我们的日志数据是不是我们关心的数，如果是，我们就往下游发送，如果不是，那么就不做任何处理
 */
public class ProcessDataBolt extends BaseBasicBolt {


    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //  1\001error main already defined in aaa.obj
        //数据自带有appId   appId对应的关心的规则信息 也有了 在map当中存着
        String logMessage = input.getStringByField("logMessage");
        String[] split = logMessage.split("\001");
        //通过调用checkRules来判断我们的数据究竟是不是我们关心的数据
        String rule = CommonUtils.checkRules(split[0], split[1]);
        if(rule !=null  &&rule != ""){
            //匹配上了我们的规则
            //哪一条数据匹配上了哪一条规则，我们都往下发
            collector.emit(new Values(rule,logMessage));

        }else{
            //没有匹配上我们的规则不做任何处理
        }



    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("rules","logMatch"));

    }
}
