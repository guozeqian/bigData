package com.ame.readboard.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class RealBoardMain {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutConigBuilder = KafkaSpoutConfig.builder("node01:9092,node02:9092,node03:9092", "itcast_order");

        kafkaSpoutConigBuilder.setOffsetCommitPeriodMs(1000);
        kafkaSpoutConigBuilder.setGroupId("itcast_order_group");
        kafkaSpoutConigBuilder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);

        KafkaSpoutConfig<String, String> kafakSpoutConfig = kafkaSpoutConigBuilder.build();


        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<String, String>(kafakSpoutConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout",kafkaSpout);
        builder.setBolt("countMoneyBolt",new CountMoneyBolt()).localOrShuffleGrouping("kafkaSpout");
        Config config = new Config();

        if(args !=null &&args.length >0){
            StormSubmitter.submitTopology(args[0],config,builder.createTopology());

        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("countMoney",config,builder.createTopology());

        }





    }

}
