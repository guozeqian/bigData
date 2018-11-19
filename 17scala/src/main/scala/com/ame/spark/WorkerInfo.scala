package com.ame.spark

//用于保存worker的注册信息
class WorkerInfo(val workerId:String,val memory:Int,val cores:Int) {
     //用于存储worker的上一次心跳时间
    var lastHeartBeatTime:Long=_

  override def toString: String = {
    s"workerId:$workerId memory:$memory cores:$cores"
  }

}
