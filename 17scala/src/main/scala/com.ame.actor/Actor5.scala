package com.ame.actor

import scala.actors.Actor

//todo:第四个例子：使用react方法替换receive来不断的接受消息
class Actor5 extends Actor {
  override def act(): Unit = {

    //使用 loop+react这种更加高效的方式去接受数据
    loop {
      react {
        case "start" => println("starting...")
        case "stop" => println("stopping...")
      }

    }
  }
}

object Actor5{
  def main(args: Array[String]): Unit = {
      //1、创建Actor对象
      val actor = new Actor5

     //2、启动actor
      actor.start()

     //3、向actor发送消息
      actor ! "start"
      actor ! "stop"
  }
}
