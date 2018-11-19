package com.ame.storm.demo1;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

public class TopologyMain  {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //将我们的spout与bolt组织起来成为一个topology提交运行
        //通过TopologyBuilder 来组织我们的spout与bolt
        TopologyBuilder builder = new TopologyBuilder();
        //设置我们的spout
        //spout的数据如何发送给哪一个bolt
        //通过参数3  来设置我们的spout的线程的数量为3  使用三个线程一起来执行我们的spout的nextTuple 方法
        builder.setSpout("randomSpout",new RandomSpout(),3);
        //设置我们的splitBolt
        //调用shuffleGrouping  定义我们的分组策略 ，也就是定义我们的数据的上游是谁
        //通过参数控制我们SplitBolt 线程的数量为3  ，就会有三个线程一起去执行  execute  方法

      //  builder.setBolt("splitBolt",new SplitBolt(),3).fieldsGrouping("randSpout",new Fields("hello","numbs"))

        builder.setBolt("splitBolt",new SplitBolt(),3).localOrShuffleGrouping("randomSpout");
        //设置我们的countBolt
        builder.setBolt("countBolt",new CountBolt(),3).localOrShuffleGrouping("splitBolt");

        //定义好了我们的数据的流向问题之后，将我们代码提交
        Config config = new Config();

        //设置我们的进程数量
        config.setNumWorkers(3);

        //集群提交模式，打包到集群上面去的时候，用这种方式进行提交
        StormTopology topology = builder.createTopology();
        if(args !=null && args.length > 0){
            //有带参数
            config.setDebug(false);
            StormSubmitter.submitTopology(args[0],config,topology);
        }else{
            config.setDebug(true);
            //本地提交模式，便于我们的开发与调试
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("wordCount",config,topology);
        }
    }
}




