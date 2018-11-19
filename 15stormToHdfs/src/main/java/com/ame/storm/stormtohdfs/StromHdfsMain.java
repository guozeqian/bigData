package com.ame.storm.stormtohdfs;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.topology.TopologyBuilder;

public class StromHdfsMain {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {


        // use "|" instead of "," for field delimiter
        RecordFormat format = new DelimitedRecordFormat()
                .withFieldDelimiter("|");

        /**
         * 文件的控制策略，使用两种方式，第一种：数据条数的多少
         * 第二种：文件的内容大小
         */
// sync the filesystem after every 1k tuples
        SyncPolicy syncPolicy = new CountSyncPolicy(1000);

// rotate files when they reach 5MB
        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f, FileSizeRotationPolicy.Units.KB);

        FileNameFormat fileNameFormat = new DefaultFileNameFormat()
                .withPath("/stormToHdfs/");

        HdfsBolt hdfsBolt = new HdfsBolt()
                .withFsUrl("hdfs://node01:8020")
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);


        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("randomOrderSpout",new RandomOrderSpout());
        topologyBuilder.setBolt("countMoneyBolt",new CountOrderMoneyBolt()).localOrShuffleGrouping("randomOrderSpout");
        topologyBuilder.setBolt("hdfsBolt",hdfsBolt).localOrShuffleGrouping("countMoneyBolt");


        Config config  = new Config();
        if(args !=null && args.length >0){
            StormSubmitter.submitTopology(args[0],config,topologyBuilder.createTopology());
        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("stormToHdfs",config,topologyBuilder.createTopology());

        }



    }
}
