package com.ame.wordCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//todo:利用scala语言开发spark的wordcount（集群运行）
object WordCount_Online {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf对象 设置applicationName
    val sparkConf: SparkConf = new SparkConf().setAppName("WordCount_Online")

    //2、创建SparkContext对象 它是所有spark程序的入口，它内部会创建DAGScheduler和TaskScheduler
    val sc = new SparkContext(sparkConf)

    //3、读取数据文件
    val data: RDD[String] = sc.textFile(args(0))

    //4、切分每一行，获取所有的单词
    val words: RDD[String] = data.flatMap(_.split(" "))

    //5、每个单词计为1
    val wordAndOne: RDD[(String, Int)] = words.map((_,1))

    //6、相同单词出现的1累加
    val result: RDD[(String, Int)] = wordAndOne.reduceByKey(_+_)

    //7、把结果数据保存到hdfs上
    result.saveAsTextFile(args(1))

    //8、关闭sparkcontext
    sc.stop()
  }
}
