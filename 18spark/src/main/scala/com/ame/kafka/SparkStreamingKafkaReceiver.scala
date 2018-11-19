package com.ame.kafka

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable

//todo：sparkStreaming整合kafka -------基于receiver接收器，使用了kafka高层次消费者api（消息的偏移量由zk去维护）
object SparkStreamingKafkaReceiver {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf对象
    val sparkConf: SparkConf = new SparkConf()
                              .setAppName("SparkStreamingKafkaReceiver")
                              .setMaster("local[4]")
      //开启WAL日志，作用：保证数据源端的安全性，后期某些rdd的分区数据丢失了，是可以通过 血统+原始数据进行恢复
                                .set("spark.streaming.receiver.writeAheadLog.enable","true")

    //2、创建SparkContext对象
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("warn")

    //3、创建StreamingContext,需要2个 参数，第一个是SparkContext对象，第二个是批处理时间间隔
    val ssc = new StreamingContext(sc,Seconds(5))

       //设置checkpoint目录，用于保存接受到的数据   实际工作中是指向HDFS目录
        ssc.checkpoint("./spark-receiver")

    //4、接受kafka中topic的数据
        //指定zk服务地址
        val zkQuorum="node1:2181,node2:2181,node3:2181"
       //指定消费者组id
       val groupId="spark-receiver"
       //指定topic相关信息  key:topic名称  value:表示一个receiver接收器使用多少个线程消费topic数据
        val topics=Map("itcast" ->1)
       //(String, String): 第一个String就是消息的key，第二个String就是消息的value
      //val kafkaDstream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zkQuorum,groupId,topics)

    //构建了3个receiver接收器去接受数据，加快数据的接受速度
    val kafkaDstreamList: immutable.IndexedSeq[ReceiverInputDStream[(String, String)]] = (1 to 3).map(x => {
      val kafkaDstream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc, zkQuorum, groupId, topics)
      kafkaDstream
    })
     //ssc.union方法 把多个receiver接受器产生的Dstream汇总成一个Dstream
      val totalKafkaDstream: DStream[(String, String)] = ssc.union(kafkaDstreamList)


    // kafkaDstream.print()

    //5、获取topic中的真实数据
      val data: DStream[String] = totalKafkaDstream.map(_._2)

    //6、切分每一行获取所有的单词
    val words: DStream[String] = data.flatMap(_.split(" "))

    //7、每个单词计为1
    val wordAndOne: DStream[(String, Int)] = words.map((_,1))

    //8、相同单词出现的1累加
    val result: DStream[(String, Int)] = wordAndOne.reduceByKey(_+_)

    //9、打印
    result.print()

    //10、开启流式计算
    ssc.start()
    ssc.awaitTermination()
  }
}
