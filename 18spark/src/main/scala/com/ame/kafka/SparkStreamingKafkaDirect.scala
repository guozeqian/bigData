package com.ame.kafka

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

//todo:sparkStreaming整合kafka -------使用消费者低级api（消息的偏移量不在由zk去维护，由客户端程序自己去维护）
object SparkStreamingKafkaDirect {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf对象
    val sparkConf: SparkConf = new SparkConf()
      .setAppName("SparkStreamingKafkaDirect")

    //2、创建SparkContext对象
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("warn")

    //3、创建StreamingContext,需要2个 参数，第一个是SparkContext对象，第二个是批处理时间间隔
    val ssc = new StreamingContext(sc, Seconds(10))

    //设置checkpoint目录 用于保存消息消费的偏移量
    ssc.checkpoint("/spark-direct")

    //4、接受kafka中的数据
    val kafkaParams = Map("bootstrap.servers" -> "node1:9092,node2:9092,node3:9092", "group.id" -> "spark-direct")
    val topics = Set("itcast")

    //通过下面这种方式获取得到的Dstream它内部的rdd分区数跟kafka中topic的分区数相等。
    val kafkaDstream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    //5、获取topic中的真实数据
    val data: DStream[String] = kafkaDstream.map(_._2)

    //6、切分每一行获取所有的单词
    val words: DStream[String] = data.flatMap(_.split(" "))

    //7、每个单词计为1
    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))

    //8、相同单词出现的1累加
    val result: DStream[(String, Int)] = wordAndOne.reduceByKey(_ + _)

    //9、打印
    result.print()

    //10、开启流式计算
    ssc.start()
    ssc.awaitTermination()
  }
}
