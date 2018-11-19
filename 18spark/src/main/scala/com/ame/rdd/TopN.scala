package com.ame.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//TODO:通过spark来实现点击流日志分析案例-----------TopN(获取访问url出现次数最多的前N位)
object TopN {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf
    val sparkConf: SparkConf = new SparkConf().setAppName("TopN").setMaster("local[2]")

    //2、创建SparkContext
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("warn")

    //3、读取数据文件
    val data: RDD[String] = sc.textFile("E:\\data\\access.log")

    //4、先过滤出正常的数据，切分每一行，获取url,每一个url计为1
    val urlAndOne: RDD[(String, Int)] = data.filter(x=>x.split(" ").length>10).map(x=>(x.split(" ")(10),1))

     //5、相同url出现的1累加
      val result: RDD[(String, Int)] = urlAndOne.reduceByKey(_+_)

     //6、按照次数降序排列
      val sortRDD: RDD[(String, Int)] = result.sortBy(_._2,false)

    //7、取出出现次数最多的前5位
     val top5: Array[(String, Int)] = sortRDD.take(5)
     top5.foreach(println)

    sc.stop()
  }
}
