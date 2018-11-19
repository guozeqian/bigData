package com.ame.wordCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//todo:利用scala语言去开发spark的wordcount程序
object WordCount {
  def main(args: Array[String]): Unit = {
    //1、创建SparkConf对象 设置applicationName和master地址  local[2]表示本地采用2个线程运行任务
      val sparkConf: SparkConf = new SparkConf().setAppName("WordCount").setMaster("local[2]")

    //2、创建SparkContext对象 它是所有spark程序的入口，它内部会创建DAGScheduler和TaskScheduler
      val sc = new SparkContext(sparkConf)

    //3、读取数据文件
      val data: RDD[String] = sc.textFile("E:\\words.txt")

    //4、切分每一行，获取所有的单词
     val words: RDD[String] = data.flatMap(_.split(" "))

    //5、每个单词计为1
      val wordAndOne: RDD[(String, Int)] = words.map((_,1))

    //6、相同单词出现的1累加
      val result: RDD[(String, Int)] = wordAndOne.reduceByKey(_+_)

        //按照单词出现的次数降序排列  sortBy默认是升序，给false就是降序
         val sortRDD: RDD[(String, Int)] = result.sortBy(x=>x._2,false)

    //7、收集打印
      val finalResult: Array[(String, Int)] = sortRDD.collect()
      finalResult.foreach(println)

    //8、关闭sparkcontext
      sc.stop()
  }
}
