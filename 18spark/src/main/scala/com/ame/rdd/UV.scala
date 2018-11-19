package com.ame.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//TODO:通过spark来实现点击流日志分析案例-----------UV
object UV {
  def main(args: Array[String]): Unit = {
     //1、创建SparkConf
      val sparkConf: SparkConf = new SparkConf().setAppName("UV").setMaster("local[2]")

    //2、创建SparkContext
     val sc = new SparkContext(sparkConf)
      sc.setLogLevel("warn")

    //3、读取数据文件
     val data: RDD[String] = sc.textFile("E:\\data\\access.log")

    //4、切分每一行获取ip地址
     val ips: RDD[String] = data.map(_.split(" ")(0))

    //5、去重统计uv
    val uv: Long = ips.distinct().count()
    println("UV:"+uv)

    sc.stop()
  }
}
