package com.ame.wifidata;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * 这个bolt主要用于监控redis当中的黑白名单，出现黑名单，马上通知对应的网安
 */
public class WifiWarningBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String wifiLine = input.getStringByField("wifiLine");
        if(null != wifiLine && wifiLine.split("@zolen@").length > 0){

            //获取数据是哪种类型，根据不同的数据类型来判断获取我们对应不同的黑名单当中的信息
            //根据不同的字段的类型，获取对应的不同的黑白名单匹配

            //数据没有匹配黑白名单，直接往下发
            String[] split = wifiLine.split("\001");
            ////  YT1013  25  YT1020 22   YT1023 51   YT1033  21   YT1034  24
            switch (split.length){
                case (25):
                    collector.emit(new Values(wifiLine,"YT1013"));
                    break;
                case (22):
                    collector.emit(new Values(wifiLine,"YT1020"));
                    break;
                case (51):
                    collector.emit(new Values(wifiLine,"YT1023"));
                    break;
                case (21):
                    collector.emit(new Values(wifiLine,"YT1033"));
                    break;
                case (24):
                    collector.emit(new Values(wifiLine,"YT1034"));
                    break;
                default:
                    break;
        }


        }








    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("writeFile","dataType"));

    }
}
