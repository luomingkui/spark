package com.luomk

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author luomingkui
  * @date 2018/6/16 下午9:31
  * @desc 利用sparkStream计算WorldCount
  *      yun search nc
  *      运行步骤：在服务端开启 nc -lk 9999
  *      然后启动该程序
  *      在服务端输入参数，在控制台就可以看见效果了
  */
object WorldCount {

  def main(args: Array[String]): Unit = {

    //创建SparkStream对象
    val conf = new SparkConf().setAppName("Streaming").setMaster("local[*]")

    //创建StreamingContext对象
    val ssc = new StreamingContext(conf,Seconds(5))

    //创建一个接收器来接受数据 DStream[String]
    val linesDStream = ssc.socketTextStream("hadoop102",9999)

    //flapMap转换为单词
    val worldDStream = linesDStream.flatMap(_.split(" "))

    //将单词转换为kv结构
    val kvDStream = worldDStream.map((_,1))

    //将相同单词个数进行合并
    val result = kvDStream.reduceByKey(_+_)

    /*val updateFunc = (values:Seq[Int], state:Option[Int]) => {

      var previousState = state.getOrElse(0)

      Some(previousState + values.sum)

    }

    val result = kvDStream.updateStateByKey[Int](updateFunc)*/

    //val result = kvDStream.reduceByKeyAndWindow((a:Int,b:Int) => a + b, (a:Int, b:Int) => a -b, Seconds(20), Seconds(10))

    result.print()

    ssc.start()

    ssc.awaitTermination()

  }

}




















