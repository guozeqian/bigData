package com.ame.sparksql


import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

//todo：将RDD转换成DataFrame--利用反射机制（就是去定义样例类）
case class Person(id: Int, name: String, age: Int)

object CaseClassSchema {
  def main(args: Array[String]): Unit = {
    //1、创建SparkSession
    val spark: SparkSession = SparkSession.builder().appName("CaseClassSchema").master("local[2]").getOrCreate()

    //2、创建SparkContext
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("warn")

    //3、读取数据创建RDD
    val rdd1: RDD[Array[String]] = sc.textFile("E:\\person.txt").map(_.split(" "))

    //4、将rdd与样例类进行关联
    val personRDD: RDD[Person] = rdd1.map(x => Person(x(0).toInt, x(1), x(2).toInt))

    //5、获取dataFrame
    import  spark.implicits._
    //手动导入隐式转换
    val personDF: DataFrame = personRDD.toDF()
    //打印schema
    personDF.printSchema()
    //展示数据
    personDF.show() //默认展示前20条数据，如果一个字符串大于20个字符，截取前20位  name=xxxxxxxxxxxxxxxxxxxxxxxxx
    //---------------------DSL风格语法-------------start
    personDF.show(1)
    println(personDF.first())
    personDF.head(3).foreach(println)

    personDF.select("name").show()
    personDF.select($"name", $"id").show()
    personDF.select(new Column("name")).show()

    personDF.select($"name", $"age", $"age" + 1).show()
    personDF.filter($"age" > 30).show()
    println(personDF.filter($"age" > 30).count())
    personDF.groupBy("age").count().show()
    //---------------------DSL风格语法-------------end

    //---------------------SQL风格语法-------------start
    personDF.createTempView("person")
    spark.sql("select * from person").show()
    spark.sql("select name,id from person").show()
    spark.sql("select * from person where age >30").show()
    spark.sql("select * from person order by age desc").show()

    //---------------------SQL风格语法-------------end

    //关闭
    sc.stop()
    spark.stop()
  }
}
