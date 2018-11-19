package com.ame.storm.logmonitor;

import com.ame.storm.logmonitor.utils.CommonUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class NotifyBolt extends BaseBasicBolt {



    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //"rules","logMatch"
        String rules = input.getStringByField("rules");
        //logMessage带上了appId
        String logMessage = input.getStringByField("logMatch");
        //发送通知给我们对应的人
        CommonUtils.notifyPeople(rules,logMessage);

        collector.emit(new Values(rules,logMessage));






    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("saveRule","saveMessage"));

    }
}
