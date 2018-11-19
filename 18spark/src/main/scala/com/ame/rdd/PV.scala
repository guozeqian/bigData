package com.ame.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//TODO:通过spark来实现点击流日志分析案例-----------PV
object PV {
  def main(args: Array[String]): Unit = {
       //1、创建SparkConf对象
      val sparkConf: SparkConf = new SparkConf().setAppName("PV").setMaster("local[2]")

      //2、创建SparkContext对象
      val sc = new SparkContext(sparkConf)

       //设置日志输出级别
       sc.setLogLevel("warn")

     //3、读取日志数据
      val data: RDD[String] = sc.textFile("E:\\data\\access.log")

    //4、统计PV
     val pv: Long = data.count()
    println("PV:"+pv)


    sc.stop()
  }
}
