package com.ame.socket

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

//todo:利用sparkStreaming接受socket数据实现所有批次单词统计结果累加
object SparkStreamingSocketTotal {

  //currentValues:表示在当前批次中相同的单词出现的所有的1（hadoop---->List(1,1,1,1)）
  //historyValues:表示每一个单词在之前所有批次中出现总次数

     //Option类型：可以表示可能存在或者不存在的值   存在Some，  不存在None
  def updateFunc(currentValues:Seq[Int],historyValues:Option[Int]):Option[Int] = {
       val newValue: Int = currentValues.sum  +  historyValues.getOrElse(0)
       Some(newValue)
  }

  def main(args: Array[String]): Unit = {
      //1、创建SparkConf
      val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingSocketTotal").setMaster("local[2]")

      //2、创建SparkContext
      val sc = new SparkContext(sparkConf)
      sc.setLogLevel("warn")

     //3、创建StreamingContext
      val ssc = new StreamingContext(sc,Seconds(5))

       //设置checkpoint目录，主要的作用:用于保存之前批次每一个单词出现的总次数
        ssc.checkpoint("./socket")

     //4、接受socket数据
      val socketTextStream: ReceiverInputDStream[String] = ssc.socketTextStream("node1",9999)

    //5、切分每一行获取所有的单词
      val words: DStream[String] = socketTextStream.flatMap(_.split(" "))

    //6、每个单词计为1
      val wordAndOne: DStream[(String, Int)] = words.map((_,1))

    //7、相同单词出现的1累加
      val result: DStream[(String, Int)] = wordAndOne.updateStateByKey(updateFunc)

    //8、打印
      result.print()

    //9、开启流式计算
      ssc.start()
      ssc.awaitTermination()
  }
}
