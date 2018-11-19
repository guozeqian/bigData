package com.ame.socket

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

//todo:利用sparkStreaming接受socket数据实现单词统计
object SparkStreamingSocket {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf对象
      val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingSocket").setMaster("local[2]")

    //2、创建SparkContext对象
      val sc = new SparkContext(sparkConf)
      sc.setLogLevel("warn")

    //3、创建StreamingContext,需要2个 参数，第一个是SparkContext对象，第二个是批处理时间间隔
      val ssc = new StreamingContext(sc,Seconds(5))

    //4、接受socket数据
       val socketTextStream: ReceiverInputDStream[String] = ssc.socketTextStream("node1",9999)

    //5、切分每一行获取所有的单词
      val words: DStream[String] = socketTextStream.flatMap(_.split(" "))

    //6、每个单词计为1
      val wordAndOne: DStream[(String, Int)] = words.map((_,1))

    //7、相同单词出现的1累加
      val result: DStream[(String, Int)] = wordAndOne.reduceByKey(_+_)

    //8、打印
      result.print()

    //9、开启流式计算
      ssc.start()
      ssc.awaitTermination()
  }
}
