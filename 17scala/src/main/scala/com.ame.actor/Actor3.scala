package com.ame.actor

import scala.actors.Actor

//todo:第二个例子：使用actor发送和接受消息
class Actor3  extends Actor{
  override def act(): Unit = {
    //接受消息
    receive{
      case "start" =>{
        println("starting...")
      }
    }

  }
}

object Actor3{
  def main(args: Array[String]): Unit = {
       //1、创建actor对象
      val actor = new Actor3

      //2、启动actor
      actor.start()

      //3、向actor发送数据
       actor ! "start"
  }
}
