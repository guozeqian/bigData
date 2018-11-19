package com.ame.actor

import scala.actors.Actor

//todo:第一个例子：实现利用actor进行并发编程
class Actor1 extends Actor{
  //重写act方法
  override def act(): Unit = {
      for(i <- 1 to 10){
        println("Actor1------"+i)
      }
  }
}

object Actor2 extends Actor{
  override def act(): Unit = {
    for(i <- 1 to 10){
      println("Actor2------"+i)
    }
  }
}

object Actor1{
  def main(args: Array[String]): Unit = {
     //1、创建actor对象
      val actor = new Actor1

    //2、启动actor
        actor.start()
        Actor2.start()
  }
}
