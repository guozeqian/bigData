package com.ame.spark

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._

//todo:利用akka中actor来实现简易版的spark通信框架-----------Master端
class Master extends Actor{
  println("Master constructor invoked")

  //定义一个Map集合用于存储worker的注册信息   key:workerId     value: WorkerInfo
     private val id2WorkerInfoMap = new mutable.HashMap[String,WorkerInfo]()

  //定义一个list集合用于存储WorkerInfo信息，主要是方便于后期按照worker的资源大小排序
    private val workerInfoList = new ListBuffer[WorkerInfo]

  //定义master定时检查的时间间隔
    val checkOutTimeInterval=15000 //15秒

  override def preStart(): Unit = {
    println("preStart method invoked")

    //master定时检查超时的worker
     //手动导入隐式转换
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, checkOutTimeInterval millis,self,CheckOutTime)
  }

  override def receive: Receive = {
    //master接受worker的注册信息
    case RegisterMessage(workerId,memory,cores) =>{
      //判断当前worker是否注册，master只接受没有注册的worker信息
       if(!id2WorkerInfoMap.contains(workerId)){
          //构建WorkerInfo对象
          val workerInfo = new WorkerInfo(workerId,memory,cores)

          //把workerinfo添加到map集合中
           id2WorkerInfoMap.put(workerId,workerInfo)

         //把workerinfo添加到list集合中
         workerInfoList +=workerInfo

         //master反馈注册成功信息给worker
          sender ! RegisteredMessage(s"workerId:$workerId 注册成功")
       }
    }

     //master接受worker的心跳信息
    case HeartBeat(workerId) =>{
      //判断当前worker是否注册，master只接受已经注册过的worker的心跳信息
       if(id2WorkerInfoMap.contains(workerId)){
         //获取workerId对应的WorkerInfo
          val workerInfo: WorkerInfo = id2WorkerInfoMap(workerId)
         //获取当前系统时间
           val lastTime: Long = System.currentTimeMillis()

         //把当前系统时间赋值给worker上一次心跳时间
          workerInfo.lastHeartBeatTime=lastTime
       }
    }

      //master接受master信息
    case CheckOutTime =>{
       //判断worker超时逻辑：当前时间 - worker上一次心跳时间 > master定时检查的时间间隔
        //获取当前时间
        val now: Long = System.currentTimeMillis()

      val outTimeWorkerInfoes: ListBuffer[WorkerInfo] = workerInfoList.filter(x => now - x.lastHeartBeatTime > checkOutTimeInterval)
        //遍历
       for(c <- outTimeWorkerInfoes){
         //获取超时的workerinfo对应的workerId
          val workerId: String = c.workerId
         //从map集合移除掉超时的worker信息
          id2WorkerInfoMap.remove(workerId)

         //从list集合移除掉超时的worker信息
          workerInfoList -=c

         println(s"workerId:$workerId 已经超时")
       }

       println("活着的worker总数："+workerInfoList.size)

      // 按照worker内存大小降序排列
      println(workerInfoList.sortBy(x=>x.memory).reverse)

    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {

       //定义master的IP地址
      val host=args(0)
       //定义master的端口
      val port=args(1)

       val configStr=
         s"""
           |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
           |akka.remote.netty.tcp.hostname = "$host"
           |akka.remote.netty.tcp.port = "$port"
         """.stripMargin
       val config: Config = ConfigFactory.parseString(configStr)
       //1、创建ActorSystem
      val masterActorSystem = ActorSystem("masterActorSystem",config)

       //2、创建master actor
      val masterActor: ActorRef = masterActorSystem.actorOf(Props(new Master),"masterActor")
  }
}
