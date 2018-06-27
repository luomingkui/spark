package com.luomk.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.DateTime

/**
  * @author luomingkui
  * @date 2018/6/15 上午9:50
  * @desc 统计每一个省份每一个小时的TOP3广告的ID  最小粒度  pro_hour_ad

  */
object Practice2 {

  def getHour(timelong: String): String = {
    val datetime = new DateTime(timelong.toLong)
    datetime.getHourOfDay.toString
  }


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

    //产生最小粒度  RDD[ ( pro_hour_ad , 1 ) ]
    val pro_hour_ad2Count = logsArray.map { x =>
      (x(1) + "_" + getHour(x(0)) + "_" + x(4), 1)
    }

    //计算每一个省份每一个小时每一个广告的点击总量  RDD[ ( pro_hour_ad , sum ) ]
    val pro_hour_ad2Sum = pro_hour_ad2Count.reduceByKey(_ + _)

    //拆分key，扩大粒度  RDD[ ( pro_hour , (ad, sum) ) ]
    val pro_hour2AdArray = pro_hour_ad2Sum.map{x =>
      val param = x._1.split("_")
      (param(0) + "_" + param(1), (param(2), x._2))
    }

    //将一个省份一个小时内的数据聚合 RDD[ ( pro_hour , Array[ (ad, sum) ] ) ]
    val pro_hour2AdGroup = pro_hour2AdArray.groupByKey()

    //直接对一个小时内的广告排序，取前三
    val pro_hour2Top3AdArray = pro_hour2AdGroup.mapValues{ x =>
      x.toList.sortWith( (x,y) => x._2 > y._2  ).take(3)
    }

    //扩大粒度， RDD[ ( pro , (hour, Array[(ad, sum)])  ) ]
    val pro2hourAdArray = pro_hour2Top3AdArray.map{x =>
      val param = x._1.split("_")
      (param(0), (param(1), x._2))
    }

    val result2 = pro2hourAdArray.groupByKey()

    result2.collectAsMap()

    //关闭SparkContext
    sc.stop()
  }

}
