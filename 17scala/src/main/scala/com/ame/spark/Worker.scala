package com.ame.spark

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.duration._

//todo:利用akka中actor来实现简易版的spark通信框架-----------Worker端
class Worker(val memory:Int,val cores:Int,val masterHost:String,val masterPort:String) extends Actor{

  println("Worker constructor invoked")

  //定义workerId
    private val workerId: String = UUID.randomUUID().toString

  //定义worker定时发送心跳时间间隔
    val sendHeartBeatInterval=10000   //10秒

  var master: ActorSelection=_

  override def preStart(): Unit = {
    println("preStart method invoked")
    //获取master引用
     master= context.actorSelection(s"akka.tcp://masterActorSystem@$masterHost:$masterPort/user/masterActor")

    //向master发送注册信息，需要通过样例类去封装注册信息(workerId,memory,cores)
     master ! RegisterMessage(workerId,memory,cores)
  }

  override def receive: Receive = {
    //worker接受master反馈的注册成功信息
    case RegisteredMessage(message) =>{
      println(message)

      //向master定时发送心跳  由于master的引用类型跟所需要的参数不一致，这里不可以直接向master发送信息，使用self（表示worker自己本身）
      //手动导入隐式转换
      import context.dispatcher
       context.system.scheduler.schedule(0 millis, sendHeartBeatInterval millis, self, SendHeartBeat)
    }

      //worker接受worker的发送信息
  case SendHeartBeat =>{
     //worker真正向master发送心跳
      master !  HeartBeat(workerId)
  }

  }
}

object Worker{
  def main(args: Array[String]): Unit = {

    //定义worker的IP地址
    val host=args(0)
    //定义worker的端口
    val port=args(1)

    //定义worker的内存
    val memory=args(2).toInt
    //定义worker的核数
    val cores=args(3).toInt

    //定义master的地址
      val masterHost=args(4)
    //定义master端口
      val masterPort=args(5)

    val configStr=
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
         """.stripMargin
    val config: Config = ConfigFactory.parseString(configStr)
    //1、创建ActorSystem
    val workerActorSystem = ActorSystem("workerActorSystem",config)

    //2、创建master actor
    val workerActor: ActorRef = workerActorSystem.actorOf(Props(new Worker(memory,cores,masterHost,masterPort)),"workerActor")
  }
}
