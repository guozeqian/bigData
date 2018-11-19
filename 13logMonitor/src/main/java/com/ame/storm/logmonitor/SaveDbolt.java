package com.ame.storm.logmonitor;

import com.ame.storm.logmonitor.domain.LogMonitorRuleRecord;
import com.ame.storm.logmonitor.utils.JdbcUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Date;

public class SaveDbolt extends BaseBasicBolt{
    /**
     * 保存数据库，将我们出发通知的日志信息保存起来
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        
        //"saveRule","saveMessage"
        String saveRule = input.getStringByField("saveRule");
        String saveMessage = input.getStringByField("saveMessage");
        String[] split = saveMessage.split("\001");
        //将日志信息保存到数据库
        LogMonitorRuleRecord logMonitorRuleRecord = new LogMonitorRuleRecord();
        logMonitorRuleRecord.setAppId(Integer.parseInt(split[0]));
        logMonitorRuleRecord.setCreateDate(new Date());
        logMonitorRuleRecord.setIsClose(1);
        logMonitorRuleRecord.setIsEmail(1);
        logMonitorRuleRecord.setIsPhone(1);
        logMonitorRuleRecord.setNoticeInfo(saveMessage);
        logMonitorRuleRecord.setRuleId(1);
        logMonitorRuleRecord.setUpdateDate(new Date());
        JdbcUtils.saveRuleRecord(logMonitorRuleRecord);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
