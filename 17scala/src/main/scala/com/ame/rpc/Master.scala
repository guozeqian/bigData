package com.ame.rpc

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

//todo:利用akka中的actor来实现2个进程间的通信-------------Master端

class Master extends Actor{
  //构造代码先运行
  println("master constructor invoked")

  //preStart方法它会在构造代码执行后被调用，并且只被调用一次
  override def preStart(): Unit = {
    println("preStart method invoked")
  }

  //receive方法它会在prestart方法执行后被调用，它会一直接受消息
  override def receive: Receive = {
    // 接受worker的注册信息
    case "connect" =>{
      println("一个worker已经注册")

      //master 反馈注册成功信息给worker
        sender ! "success"


    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {

      //定义master的IP地址
      val host=args(0)

      //定义master的端口
      val port=args(1)

      //准备配置对象所需要的字符串
      val configStr=
        s"""
          |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
          |akka.remote.netty.tcp.hostname = "$host"
          |akka.remote.netty.tcp.port = "$port"
        """.stripMargin

      //创建Config对象 通过ConfigFactory.parseString解析字符串获取Config对象
          val config=ConfigFactory.parseString(configStr)

       //1、创建ActorSystem，它负责创建和监督actor
          val masterActorSystem = ActorSystem("masterActorSystem",config)

       //2、创建master actor
          val masterActor: ActorRef = masterActorSystem.actorOf(Props(new Master),"masterActor")

      //3、测试 向mater actor发送消息
        //masterActor ! "connect"
  }
}
