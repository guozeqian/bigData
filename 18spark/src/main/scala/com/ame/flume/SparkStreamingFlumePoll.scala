package com.ame.flume

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.flume.{FlumeUtils, SparkFlumeEvent}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

//todo：利用sparkStreaming整合flume-------Poll拉模式整合
object SparkStreamingFlumePoll {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf对象
    val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingFlumePoll").setMaster("local[2]")

    //2、创建SparkContext对象
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("warn")

    //3、创建StreamingContext,需要2个 参数，第一个是SparkContext对象，第二个是批处理时间间隔
    val ssc = new StreamingContext(sc,Seconds(5))

    //4、通过拉模式接受flume的数据
   val pollingStream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils.createPollingStream(ssc,"node1",8888)

     //接受多台flume收集到的数据
//        val addresses=List(new InetSocketAddress("node1",8888),new InetSocketAddress("node2",8888),new InetSocketAddress("node3",8888))
//       val stream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils.createPollingStream(ssc,addresses,StorageLevel.MEMORY_AND_DISK_SER_2)
//
    //5、获取flume中数据  flume中数据传输的最小单元是一个event：{"headers":xxxxx,"body":xxxxxx}
         //获取body中的数据
    val data: DStream[String] = pollingStream.map(x=>new String(x.event.getBody.array()))

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
