package com.luomk.sql

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author luomingkui
  * @date 2018/6/15 上午9:36
  * @desc
  */
object Praction1 {

  def main(args: Array[String]): Unit = {

    //创建sparkConf对象

    val conf = new SparkConf().setAppName("practice").setMaster("local[*]")

    //创建sparkcontext对象
    val sc = new SparkContext(conf)

    //需求：统计每一个省份点击TOP3的广告ID

    //读取数据 RDD[String]
    val logs = sc.textFile("./agent.log")

    //将RDD中的String转换为数组 RDD[Array[String]]
    val logsArray = logs.map(x => x.split(" "))

    //提取相应的数据，转换粒度 RDD[( pro_adid, 1 )]
     val proAndAd2Count = logsArray.map(x => (x(1) + "_" + x(4), 1))

     //将每一个省份每一个广告的所有点击量聚合 RDD[( pro_adid, sum )]
     val proAndAd2Sum = proAndAd2Count.reduceByKey((x, y) => x + y)

     //将粒度扩大， 拆分key， RDD[ ( pro, (sum, adid) ) ]
     val pro2AdSum = proAndAd2Sum.map { x => val param = x._1.split("_"); (param(0), (param(1), x._2)) }

     //将每一个省份所有的广告合并成一个数组 RDD[ (pro, Array[ (adid, sum) ]) ]
     val pro2AdArray = pro2AdSum.groupByKey()

     //排序取前3                                     sortWith(lt: (A, A) => Boolean)
     val result = pro2AdArray.mapValues(values => values.toList.sortWith((x, y) => x._2 > y._2).take(3))

     //行动操作
    result.collectAsMap()

    //关闭SparkContext
    sc.stop()

  }

}
