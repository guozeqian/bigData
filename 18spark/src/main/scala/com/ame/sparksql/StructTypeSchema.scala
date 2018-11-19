package com.ame.sparksql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

//todo:将RDD转换成DataFrame----通过使用StructType类型来指定schema
object StructTypeSchema {
  def main(args: Array[String]): Unit = {
     //1、创建SparkSession
      val spark: SparkSession = SparkSession.builder().appName("StructTypeSchema").master("local[2]").getOrCreate()

     //2、创建SparkContext
       val sc: SparkContext = spark.sparkContext
      sc.setLogLevel("warn")

    //3、读取数据文件
      val rdd1: RDD[Array[String]] = sc.textFile("E:\\person.txt").map(x=>x.split(" "))

    //4、需要把rdd1与Row进行关联
     val rowRDD: RDD[Row] = rdd1.map(x=>Row(x(0),x(1),x(2).toInt))

    //5、StructType指定schema
    val schema =
      StructType(
          StructField("id", StringType, true) ::
          StructField("name", StringType, false) ::
          StructField("age", IntegerType, false) :: Nil)


      val dataFrame: DataFrame = spark.createDataFrame(rowRDD,schema)

      dataFrame.printSchema()
      dataFrame.show()

      dataFrame.createTempView("person")
      spark.sql("select * from person ").show()
      spark.sql("select * from person where id=1").show()

     //关闭
     sc.stop()
     spark.stop()

  }
}
