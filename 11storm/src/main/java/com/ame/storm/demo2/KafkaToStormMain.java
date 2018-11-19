package com.ame.storm.demo2;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class KafkaToStormMain {

    /**
     * 程序的入口
     * @param args
     */
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //获取到了一个内部类
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutConfigBuilder = KafkaSpoutConfig.builder("node01:9092,node02:9092,node03:9092", "test");

        //所有关于kafka的设置都在kafkaSpoutConfigBuilder 里面配置
        //控制kafka的offset的消费策略
        kafkaSpoutConfigBuilder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);
        kafkaSpoutConfigBuilder.setGroupId("kafkaToStorm");
        kafkaSpoutConfigBuilder.setOffsetCommitPeriodMs(1000L);
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = kafkaSpoutConfigBuilder.build();
        //kafkaSpout需要KafkaSpoutConfig
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<String, String>(kafkaSpoutConfig);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout",kafkaSpout);
        builder.setBolt("printlnBolt",new PrintlnBolt()).localOrShuffleGrouping("kafkaSpout");

        Config config = new Config();
        if(null != args && args.length >0){
            //集群提交模式
            StormSubmitter.submitTopology(args[0],config,builder.createTopology());

        }else{
            //本地提交模式
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("localStorm",config,builder.createTopology());
        }
    }
}
