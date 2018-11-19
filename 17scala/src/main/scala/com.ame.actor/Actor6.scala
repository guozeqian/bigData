package com.ame.actor

import scala.actors.{Actor, Future}

//todo:第五个例子：结合case class 样例类去封装消息

case class ASyncMessage(id:Int,message:String)  //异步消息
case class SyncMessage(id:Int,message:String)   //同步消息
case class ReplyMessage(id:Int,message:String) //返回的结果数据
class Actor6  extends Actor{
  override def act(): Unit = {
    loop{
      react{
         //匹配异步消息
        case ASyncMessage(id,message) =>{
           println(s"id:$id message:$message")

           //可以通过sender方法拿到消息发送方的引用，可以通过这个引用把消息返回回去
           sender ! ReplyMessage(id,"异步有返回值的消息处理成功")
        }

        //匹配同步消息
        case SyncMessage(id,message) =>{
          println(s"id:$id message:$message")

          //可以通过sender方法拿到消息发送方的引用，可以通过这个引用把消息返回回去
          sender ! ReplyMessage(id,"同步等待返回值的消息处理成功")
        }
      }
    }

  }
}

object Actor6{
  def main(args: Array[String]): Unit = {
     //1、创建Actor对象
      val actor = new Actor6
     //2、启动actor
      actor.start()

     //3、可以向actor发送消息
        //3.1 发送一个异步无返回值的消息
           actor !  ASyncMessage(1,"我是一个异步无返回值的消息")

        //3.2 发送一个异步有返回值的消息
           val future: Future[Any] = actor !! ASyncMessage(2,"我是一个异步有返回值的消息")
            //获取future中结果数据
            val result: Any = future.apply()
             println(result)

       //3.3 发送一个同步等待返回值的消息
          val reply: Any = actor !? SyncMessage(3,"我是一个同步等待返回值的消息")
          println(reply)
  }
}
