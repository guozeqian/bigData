package com.ame.storm.wordCount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class WordCountMain {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("randomOrderSpout",new RandomWordSpout());
        topologyBuilder.setBolt("countMoneyBolt",new SplitBolt()).localOrShuffleGrouping("randomOrderSpout");
        topologyBuilder.setBolt("hdfsBolt",new CountNumBolt()).localOrShuffleGrouping("countMoneyBolt");


        Config config  = new Config();

        //运算异或的线程默认是1个，如果数据量太大，一个线程忙不过来，那么就需要加线程
        config.setNumAckers(5);
        //开启ack机制  spout发送数据的时候带上msgId  设置我们的ack的线程数大于0


        //如何关闭ack机制：
        //发送消息的时候不用带msgID，设置ack的线程数为0

//如果内存池当中有很多的状态没有清掉，我们可以设置这个清掉的临界值，storm的程序就会停掉，不要再发送数据了
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 50000);
        //也可以设置我们的消息的超时时间，默认是30s钟
     //   Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS

        if(args !=null && args.length >0){
            StormSubmitter.submitTopology(args[0],config,topologyBuilder.createTopology());
        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("stormToHdfs",config,topologyBuilder.createTopology());

        }



    }

}
