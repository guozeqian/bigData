package com.ame.actor

import scala.actors.{Actor, Future}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

//todo:利用actor来实现一个单机版的wordcount，把多个文件作为输入，先进行局部汇总，然后进行全局汇总

case class SubmitTask(fileName:String)
case class ResultTask(result:Map[String, Int])

class Task extends Actor{
  override def act(): Unit = {
    loop{
      react{
        case SubmitTask(fileName) =>{
          //1、读取文件数据
            val data: String = Source.fromFile(fileName).mkString
            //println(data)

          //2、按照换行符切分 window下换行符 \r\n  linux下换行符 \n  Mac下换行符 \r
            val lines: Array[String] = data.split("\r\n")
            //println(lines.toBuffer)

          //3、获取所有的单词
           val words: Array[String] = lines.flatMap(_.split(" "))
           //println(words.toBuffer)

          //4、每个单词计为1
           val wordAndOne: Array[(String, Int)] = words.map((_,1))
           //println(wordAndOne.toBuffer)

          //5、按照单词进行分组
            val groupByWord: Map[String, Array[(String, Int)]] = wordAndOne.groupBy(_._1)
            //for((k,v)<-groupByWord)println(k+" -> "+v.toBuffer)

          //6、获取每一个单词出现的次数
            val result: Map[String, Int] = groupByWord.mapValues(_.length)
            //for((k,v) <- result) println(k+" -> "+v)

          sender  ! ResultTask(result)
        }
      }
    }
  }
}

object WordCount{
  def main(args: Array[String]): Unit = {
    //定义一个set集合，用于保存每一个文件产生的Future
    val futureSet = new mutable.HashSet[Future[Any]]()

    //定义一个list集合，用于保存每一个Future真实数据
      val resultList = new ListBuffer[ResultTask]

    //准备数据文件
      val files=Array("E:\\aa.txt","E:\\bb.txt","E:\\cc.txt")
    //遍历
      for(f <- files){
        //1、创建actor
        val task = new Task
        //2、启动actor
        task.start()
        //3、向actor发送消息
        val reply: Future[Any] = task !! SubmitTask(f)

        //把future添加到set集合中
        futureSet +=reply
      }

      //遍历set集合
      while(futureSet.size>0){
        //过滤出已经有真正数据的future
          val completedFutureSet: mutable.HashSet[Future[Any]] = futureSet.filter(x =>x.isSet)
           //遍历
          for( c <- completedFutureSet){
             //获取future中的数据
             val apply: Any = c.apply()
             //把结果数据添加到list集合中
              resultList +=apply.asInstanceOf[ResultTask]

             //从futureset中移除掉把数据添加到list集合中Future
             futureSet -=c
          }


      }

    println(resultList.map(x=>x.result).flatten.groupBy(x=>x._1).mapValues( x =>x.foldLeft(0)(_+_._2)))


  }
}
