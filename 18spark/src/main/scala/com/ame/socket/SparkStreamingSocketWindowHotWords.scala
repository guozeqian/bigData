package com.ame.socket

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

//todo:利用开窗函数来实现一定时间内的热门词汇
object SparkStreamingSocketWindowHotWords {
  def main(args: Array[String]): Unit = {
      //1、创建SparkConf
      val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingSocketWindowHotWords").setMaster("local[2]")

      //2、创建SparkContext
      val sc = new SparkContext(sparkConf)
      sc.setLogLevel("warn")

     //3、创建StreamingContext
      val ssc = new StreamingContext(sc,Seconds(5))

    //4、接受socket数据
    val socketTextStream: ReceiverInputDStream[String] = ssc.socketTextStream("node1",9999)

    //5、切分每一行获取所有的单词
    val words: DStream[String] = socketTextStream.flatMap(_.split(" "))

    //6、每个单词计为1
    val wordAndOne: DStream[(String, Int)] = words.map((_,1))

    //7、相同单词出现的1累加
    val result: DStream[(String, Int)] = wordAndOne.reduceByKeyAndWindow((x:Int,y:Int)=>x+y,Seconds(10),Seconds(10))

    //按照单词出现的次数降序排列
    val sortDstream: DStream[(String, Int)] = result.transform(rdd => {
      //按照单词次数降序
      val sortRDD: RDD[(String, Int)] = rdd.sortBy(_._2, false)
      //取出次数最多的前3位
      val top3: Array[(String, Int)] = sortRDD.take(3)
      println("---------------top3--------------start")
      top3.foreach(println)
      println("---------------top3--------------end")
      sortRDD
    })

    //8、打印
    sortDstream.print()

    //9、开启流式计算
    ssc.start()
    ssc.awaitTermination()
  }
}
