package com.ame.wifidata;

import com.ame.wifidata.utils.FileOperateUtils;
import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * 这个bolt里面主要负责的就是将我们的数据写入到本地文件系统，
 * 然后写到一定大小的时候，上传到hdfs对应的文件夹下面去
 * 再写一个定时任务的功能，扫描昨天的文件夹下面有没有文件，如果有，上传到hdfs的对应的目录下面去
 */
public class WifiWriteFileBolt extends BaseBasicBolt {

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        config.put(config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,5);
        return config;
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String writeFile = input.getStringByField("writeFile");
        String dataType = input.getStringByField("dataType");
        //按照数据类型将我们的数据写入到本地文件里面去
        if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)  && input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)){
            //   定时任务发送出来的tuple
            //在这里获取当前的时间，然后找到昨天的文件本地存放的目录，然后将昨天的数据上传到hdfs的对应的文件夹下面去
            FileOperateUtils.uploadYestData();
        }else{
            //上游发送的tuple
            try {
                FileOperateUtils.meargeToLargeFile(dataType,writeFile);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }





    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
