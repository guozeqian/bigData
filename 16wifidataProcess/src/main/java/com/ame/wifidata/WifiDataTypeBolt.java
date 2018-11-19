package com.ame.wifidata;

import com.ame.wifidata.utils.FileOperateUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.File;

/**
 * 主要负责过滤一些字段长度不够的脏数据
 */
public class WifiDataTypeBolt extends BaseBasicBolt{

    private File file = new File("/export/error.txt");


    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String line = input.getString(4);
        String[] split = line.split("@zolen@");
        //  YT1013  25  YT1020 22   YT1023 51   YT1033  21   YT1034  24
       int length =  split.length;
        if(length == 25  || length ==22 || length ==51 || length ==21 || length == 24){
            //正常数据
            collector.emit(new Values(line.replace("@zolen@","\001")));
        }else{
            //异常数据,将我们的异常数据全部写入到本地的一个文件里面去
            FileOperateUtils.writeLine(file,line);



        }










    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("wifiLine"));
    }
}
