package com.ame.sparksql

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

//todo:利用sparksql加载mysql表中的数据
object DataFromMysql {
  def main(args: Array[String]): Unit = {
      //1、创建SparkSession
     val spark: SparkSession = SparkSession.builder().appName("DataFromMysql").master("local[2]").getOrCreate()


     //2、读取mysql表中的数据
        //2.1 定义url
            val url="jdbc:mysql://node1:3306/spark"
       //2.2 定义表名
            val table="iplocation"
       //2.3 定义mysql服务属性
            val properties = new Properties()
            properties.setProperty("user","root")
            properties.setProperty("password","123456")

     val mysqlDF: DataFrame = spark.read.jdbc(url,table,properties)

       mysqlDF.printSchema()
       mysqlDF.show()

       mysqlDF.createTempView("iplocation")
       spark.sql("select * from iplocation").show()
       spark.sql("select * from iplocation where total_count>1000").show()


     spark.stop()
  }
}
