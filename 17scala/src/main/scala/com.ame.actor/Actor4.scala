package com.ame.actor

import scala.actors.Actor

//todo:第三个例子：实现actor不断发送和接受消息
class Actor4  extends Actor{
  override def act(): Unit = {
    //使用while(true) +receive方法不断的接受数据
    while (true) {
      receive {
        case "start" => println("starting...")
        case "stop" => println("stopping...")
      }
    }
  }
}

object Actor4{
  def main(args: Array[String]): Unit = {
      //1、创建Actor对象
      val actor = new Actor4

      //2、启动actor
      actor.start()

      //3、向actor发送消息
      actor ! "start"
      actor ! "stop"
  }
}
