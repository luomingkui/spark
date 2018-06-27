package com.luomk.sql

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * @author luomingkui
  * @date 2018/6/15 下午8:51
  * @desc
  */
class Accumulator(numPar:Int) extends Partitioner{
  override def numPartitions: Int = numPar

  override def getPartition(key: Any): Int = {
    val ckey = key.toString
    ckey.substring(ckey.length-1).toInt % numPar
  }
}


object Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("partittoner").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val data = sc.makeRDD(List("aa.2","cc.2","ff.2","dd.3")).map((_,1))

    val result  = data.partitionBy(new Accumulator(8))

    result.mapPartitionsWithIndex((index,items) => Iterator(index+":" + items.mkString("|"))).collect().foreach(println _)

    sc.stop()
  }
}
