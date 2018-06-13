package com.luomk
import org.apache.spark.{SparkConf, SparkContext}
/**
  * @author luomingkui
  * @date 2018/6/13 下午3:20
  * @desc
  */
object sparkcore_worldcount {
  def main(args: Array[String]): Unit = {
    //新建sparkconf对象
    // 在本地运行
    // val conf = new SparkConf().setMaster("local[*]").setAppName("sparkcore_worldcount")
    //打jar包上集群运行
    val conf = new SparkConf().setAppName("sparkcore_worldcount")
    //创建sparkcontext
    val sc = new SparkContext(conf)
    //读取数据
    val textfile = sc.textFile("./WorldCount")
    //按照空格进行切分
    val worlds = textfile.flatMap(_.split(" "))
    //转换为k v 结构
    val k2v = worlds.map((_,1))
    //将形同的key进行合并
    val result = k2v.reduceByKey(_+_)
    //输出结果
    result.collect().foreach(println _)
    //关闭连接
    sc.stop()
  }
}
