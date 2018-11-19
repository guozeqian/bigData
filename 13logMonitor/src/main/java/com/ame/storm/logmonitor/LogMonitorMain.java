package com.ame.storm.logmonitor;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class LogMonitorMain {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        //kafka整合
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutConfigBuilder = KafkaSpoutConfig.builder("node01:9092,node02:9092,node03:9092", "log_monitor");
        kafkaSpoutConfigBuilder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);
        kafkaSpoutConfigBuilder.setGroupId("logMonitoryGroup");
        kafkaSpoutConfigBuilder.setOffsetCommitPeriodMs(500);
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = kafkaSpoutConfigBuilder.build();
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafakSpout", kafkaSpout);


        builder.setBolt("tickTimeBolt", new TickUpdateRuleBolt()).localOrShuffleGrouping("kafakSpout");
        builder.setBolt("processDataBolt", new ProcessDataBolt()).localOrShuffleGrouping("tickTimeBolt");
        builder.setBolt("notifyBolt", new NotifyBolt()).localOrShuffleGrouping("processDataBolt");
        builder.setBolt("saveDBolt", new SaveDbolt()).localOrShuffleGrouping("notifyBolt");

        Config config = new Config();
        if (args != null && args.length > 0) {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("logMonitor", config, builder.createTopology());
        }
    }

}
