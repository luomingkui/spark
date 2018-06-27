package com.luomk.sql

import org.apache.spark.{SparkConf, SparkContext}
/**
  * @author luomingkui
  * @date 2018/6/15 下午9:34
  * @desc
  */
object Accu {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("partittoner").setMaster("local[*]")
    val sc = new SparkContext(conf)



    var sum = sc.accumulator(0)

    val rdd = sc.makeRDD(Array(1,2,3,4,5))

    rdd.map{ x =>
      sum += x
      x
    }.collect()


    print(sum.value)

    sc.stop()
  }

}
