package com.ame.sparksql

import java.util.Properties

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

//todo:利用sparksql把结果数据写入到mysql表中

case class Student(id:Int,name:String,age:Int)
object Data2Mysql {
  def main(args: Array[String]): Unit = {
      //1、创建SparkSession
      val spark: SparkSession = SparkSession.builder().appName("Data2Mysql").getOrCreate()

     //2、创建SparkContext
      val sc: SparkContext = spark.sparkContext
     sc.setLogLevel("warn")

    //3、读取文件数据
      val rdd1: RDD[Array[String]] = sc.textFile(args(0)).map(_.split(" "))

    //4、rdd与样例类关联
      val studentRDD: RDD[Student] = rdd1.map(x=>Student(x(0).toInt,x(1),x(2).toInt))

    //5、将rdd转换成dataFrame
    import  spark.implicits._
       val studentDF: DataFrame = studentRDD.toDF()

    //5、打印schema
      studentDF.printSchema()
      studentDF.show()

      studentDF.createTempView("student")
       //把年龄大于30的用户过滤出来
      val result: DataFrame = spark.sql("select * from student where age >30")

       //把结果数据写入到mysql表中
           //2.1 定义url
             val url="jdbc:mysql://node1:3306/spark"
          //2.2 定义表名
            val table=args(1)
         //2.3 定义mysql服务属性
            val properties = new Properties()
            properties.setProperty("user","root")
            properties.setProperty("password","123456")
   //mode:表示数据插入的模式
        //方法中可以传入4个参数
          //overwrite:表示覆盖。如果表事先不存在，它会帮我们创建
         //append  : 表示追加，如果表事先不存在，它会帮我们创建
         //ignore :表示忽略 ，如果表事先存在，它不会进行数据插入，直接忽略
         //error  :默认选项，表示如果表存在就报错
    result.write.mode("append").jdbc(url,table,properties)

    //关闭
      sc.stop()
      spark.stop()

  }
}
