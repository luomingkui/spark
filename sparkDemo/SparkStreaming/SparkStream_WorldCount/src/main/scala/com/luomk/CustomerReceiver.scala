package com.atguigu.stream

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

//需要继承一个类
class CustomerReceiver(host:String, port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){

  //接收器启动的时候
  override def onStart(): Unit = {

    new Thread("socket Receiver") {
      override def run(): Unit = {
        reveice()
      }
    }.start()

  }



  def reveice(): Unit ={

    //创建Socket连接
    var socket:Socket = null
    var input:String = null

    try{

      socket = new Socket(host,port)

      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))

      //读取第一条数据
      input = reader.readLine()

      while (!isStopped() && input!=null ){
        //数据提交给框架
        store(input)

        input = reader.readLine()
      }

      reader.close()
      socket.close()

      restart("restart")

    }catch {
      case e:java.net.ConnectException => restart("restart")
      case t:Throwable => restart("restart")
    }

  }


  //接收器停止的时候，主要做资源的销毁
  override def onStop(): Unit = {}
}


object CountWorld3{
  def main(args: Array[String]): Unit = {

    //创建SparkConf对象
    val conf = new SparkConf().setAppName("Streaming").setMaster("local[*]")

    //创建StreamingContext对象
    val ssc = new StreamingContext(conf, Seconds(5))

    //创建一个接收器来接受数据 DStream[String]
    //  val linesDStream = ssc.socketTextStream("hadoop102",9999)

    val linesDStream = ssc.receiverStream(new CustomerReceiver("hadoop102",9999))

    val result = linesDStream.transform{ rdd =>
      val words = rdd.flatMap(_.split(" "))
      val k2v = words.map((_,1))
      val result = k2v.reduceByKey(_+_)
      result
    }

    /*//flatMap转换成为单词 DStream[String]
    val wordsDStream = linesDStream.flatMap(_.split(" "))
    //将单词转换为KV结构 DStream[(String,1)]
    val kvDStream = wordsDStream.map((_,1))
    //将相同单词个数进行合并
    val result = kvDStream.reduceByKey(_+_)*/

    result.print()

    ssc.start()
    ssc.awaitTermination()

  }
}
