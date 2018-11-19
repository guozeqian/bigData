package com.ame.storm.ticktime;

import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 在这个里面实现我们数据每隔五秒钟打印一次当前时间
 */
public class TickTimeBolt extends BaseBasicBolt {

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config   = new Config();
        //设置我们的配置参数的属性，每隔五秒钟，发送一个系统的tuple出来
        //设置我们每隔五秒钟，发送  __system    __tick 到execute方法去执行
        config.put(config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,5);
        return config;
    }

    /*
        这个execute方法会反复不断地被调用，上游有数据就会调用execute方法，数据都封装在tuple里面
           如果上游每隔五秒钟发送一条数据，封装在tuple里面就可以实现定时的功能，这种方式可不可取？？？？
         每隔五秒钟storm系统发送一个tuple过来
         */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //每隔五秒钟发送一个系统的tuple出来，会调用这个方法，上游发送的数据，也会调用这个方法

        //判断我们的tuple究竟是从系统发送过来的还是从上游发送过来的
        if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) &&input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(simpleDateFormat.format(new Date()));
        }else{
            //上游发送的tuple
            String names = input.getStringByField("names");//hello 12
            //将我们上游发送过来的数据发送到jdbcBolt里面去，然后jdbcBolt插入到数据库当中去
            //发送到下游的字段个数，要跟数据库的字段个数保持一致，
            //发送到下游的数据的类型，要跟数据库的字段类型保持一致
            String[] split = names.split(" ");
            collector.emit(new Values(null,split[0],Integer.parseInt(split[1])));
       //     collector.emit()
        }
    }

    /**
     * declarer  申明的字段名称，要与数据库的字段名称保持一致
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("userId","name","age"));
    }
}
