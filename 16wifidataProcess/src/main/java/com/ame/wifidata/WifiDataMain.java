package com.ame.wifidata;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class WifiDataMain {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutConfigBuilder = KafkaSpoutConfig.builder("node01:9092,node02:9092,node03:9092", "wifidata");
        kafkaSpoutConfigBuilder.setGroupId("wifiDataGroup");
        kafkaSpoutConfigBuilder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);
        kafkaSpoutConfigBuilder.setOffsetCommitPeriodMs(500);
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = kafkaSpoutConfigBuilder.build();


        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<String,String>(kafkaSpoutConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout",kafkaSpout);
        builder.setBolt("wifiDataTypeBolt",new WifiDataTypeBolt()).localOrShuffleGrouping("kafkaSpout");
        builder.setBolt("wifiWarningBolt",new WifiWarningBolt()).localOrShuffleGrouping("wifiDataTypeBolt");
        builder.setBolt("wifiWriteFileBolt",new WifiWriteFileBolt()).localOrShuffleGrouping("wifiWarningBolt");
        Config config  = new Config();
        if(null != args && args.length > 0){
            config.setDebug(true);
            StormSubmitter submitter = new StormSubmitter();
            submitter.submitTopology(args[0],config,builder.createTopology());
        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("wifiMonitor",config,builder.createTopology());
        }
















    }

}
