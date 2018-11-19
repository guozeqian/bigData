package com.ame.rdd

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//todo:利用spark实现ip地址查询
object Iplocation {

  //ip地址转换成Long   192.168.200.100
  def ip2Long(ip: String): Long = {
     val ips: Array[String] = ip.split("\\.")
     var ipNum:Long=0L
    //遍历数组
    for(i<- ips){
     ipNum= i.toLong |  ipNum << 8L
    }
    ipNum
  }

  def binarySearch(ipNum: Long, broadCastValue: Array[(String, String, String, String)]): Int = {
    //定义开始下标
    var start=0
    //定义结束下标
    var end=broadCastValue.length-1

    while(start <= end){
      val middle=(start+end)/2

      if(ipNum >= broadCastValue(middle)._1.toLong  && ipNum <= broadCastValue(middle)._2.toLong){
         return middle
      }

      if(ipNum < broadCastValue(middle)._1.toLong){
          end=middle-1
      }

      if(ipNum >broadCastValue(middle)._2.toLong){
         start=middle+1
      }

    }
    -1
  }

  def data2mysql(iter:Iterator[((String,String), Int)]) = {
    //定义数据库连接
      var conn:Connection=null
    //定义PreparedStatement
      var ps:PreparedStatement=null
     val sql="insert into iplocation(longitude,latitude,total_count) values(?,?,?)"

    conn=DriverManager.getConnection("jdbc:mysql://192.168.200.100:3306/spark","root","123456")
    ps=conn.prepareStatement(sql)

     //遍历迭代器
      try {
        iter.foreach(line => {
          //给占位符赋值
          ps.setString(1, line._1._1)
          ps.setString(2, line._1._2)
          ps.setLong(3, line._2)
          //执行sql语句
          ps.execute()
        })
      } catch {
        case e:Exception  => println(e)
      } finally {
        if(ps !=null){
          ps.close()
        }
        if(conn!=null){
          conn.close()
        }
      }

  }

  def main(args: Array[String]): Unit = {
     //1、创建SparkConf
      val sparkConf: SparkConf = new SparkConf().setAppName("Iplocation").setMaster("local[2]")

    //2、创建SparkContext
      val sc = new SparkContext(sparkConf)
      sc.setLogLevel("warn")

    //3、读取城市ip段信息数据，获取 (ip开始数字、ip结束数字、经度、维度)
     val city_ip_rdd: RDD[(String, String, String, String)] = sc.textFile("E:\\data\\ip.txt").map(x=>x.split("\\|")).map(x=>(x(2),x(3),x(x.length-2),x(x.length-1)))

       //把城市ip信息数据，通过广播变量下发到每一个worker节点
        val city_ip_broadcast: Broadcast[Array[(String, String, String, String)]] = sc.broadcast(city_ip_rdd.collect())

    //4、读取日志数据 获取所有的ip地址
      val ips_rdd: RDD[String] = sc.textFile("E:\\data\\20090121000132.394251.http.format").map(x=>x.split("\\|")(1))

    //5、遍历ips_rdd  获取每一个ip地址，然后转换成Long类型的数字，最后通过二分查找去匹配
    val result: RDD[((String, String), Int)] = ips_rdd.mapPartitions(iter => {
      //获取广播变量的值
      val broadCastValue: Array[(String, String, String, String)] = city_ip_broadcast.value
      //遍历迭代器
      iter.map(ip => {
        //把ip地址转换成Long类型数字
        val ipNum: Long = ip2Long(ip)
        // 通过二分查询获取long类型的数字在数组中下标
        val index: Int = binarySearch(ipNum, broadCastValue)

        //获取下标对应的值
        val value: (String, String, String, String) = broadCastValue(index)

        //把结果数据封装在一个元组中 ((经度，维度)，1)
        ((value._3, value._4), 1)
      })
    })
    //相同经度维度出现的1累加
    val finalResult: RDD[((String, String), Int)] = result.reduceByKey(_+_)

    //打印
      finalResult.foreach(println)

    //保存结果数据到mysql表中
      finalResult.foreachPartition(data2mysql)


    sc.stop()

  }
}
