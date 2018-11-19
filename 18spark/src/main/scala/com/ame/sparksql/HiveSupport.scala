package com.ame.sparksql

import org.apache.spark.sql.SparkSession

//todo:利用sparksql操作hivesql
object HiveSupport {
  def main(args: Array[String]): Unit = {
     //1、创建SparkSession
      val spark: SparkSession = SparkSession.builder()
                                            .appName("HiveSupport")
                                            .master("local[2]")
                                            .enableHiveSupport() //开启对hivesql支持
                                            .getOrCreate()

    //2、利用sparkSession操作hivesql
       //2.1 创建一个hive表
         //spark.sql("create table user(id int,name string,age int) row format delimited fields terminated by ','")

       //2.2 加载数据到表中
         //spark.sql("load data local inpath './data/user.txt' into table user ")

      //2.3 查询
        spark.sql("select * from user").show()


     spark.stop()
  }
}
