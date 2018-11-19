package com.ame.rpc

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

//todo:利用akka中actor模式来实现2个进程间的通信------------Worker端
class Worker extends Actor{
  println("worker constructor invoked")

  override def preStart(): Unit = {
    println("preStart method invoked")
    //获取master引用
    //通过ActorContext上下文对象，调用actorSelection方法，需要一个字符串，这个字符串中就封装了获取master的一些条件
    //需要的条件：1、通信协议 2、master的ip地址 3、master端口 4、创建master 老大 5、层级关系 6、master actor名称

    val master: ActorSelection = context.actorSelection("akka.tcp://masterActorSystem@192.168.25.39:8888/user/masterActor")

    //master引用向master actor发送信息
    master ! "connect"

  }

  override def receive: Receive = {
    //worker接受master反馈的信息
    case "success" =>{
      println("worker注册成功")
    }
  }
}

object Worker{
  def main(args: Array[String]): Unit = {

      //定义worker的IP地址
      val host=args(0)

      //定义worker的端口
      val port=args(1)

     //准备配置信息的字符串
      val configStr=
        s"""
          |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
          |akka.remote.netty.tcp.hostname = "$host"
          |akka.remote.netty.tcp.port = "$port"
        """.stripMargin

    //创建Config对象
      val config=ConfigFactory.parseString(configStr)

    //1、创建ActorSystem
        val workerActorSystem = ActorSystem("workerActorSystem",config)

     //2、创建Worker actor
      val workerActor: ActorRef = workerActorSystem.actorOf(Props(new Worker),"workerActor")

     //3、向worker actor发送消息
      //workerActor ! "connect"
  }
}
