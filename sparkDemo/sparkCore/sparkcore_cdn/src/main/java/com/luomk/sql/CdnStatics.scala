package com.luomk.sql

import org.apache.spark.{SparkConf, SparkContext}


/**
  * @author luomingkui
  * @date 2018/6/15 下午2:12
  * @desc
  * 1、计算每一个IP的访问次数   
  * (114.55.227.102,9348)
  * 2、计算每一个视频访问的IP数
  * 视频：141081.mp4 独立IP数:2393
  *    3、统计每小时CDN的流量  
  * 00时 CDN流量=14G
  */

//源数据字段对应解释
//IP 命中率 响应时间 请求时间 请求方法 请求URL    请求协议 状态吗 响应大小 referer 用户代理
//100.79.121.48 HIT 33 [15/Feb/2017:00:00:46 +0800] "GET http://cdn.v.abc.com.cn/videojs/video.js HTTP/1.1" 200 174055 "http://www.abc.com.cn/" "Mozilla/4.0+(compatible;+MSIE+6.0;+Windows+NT+5.1;+Trident/4.0;)"
object CdnStatics {

  //匹配IP地址
  val IPPattern = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))".r


  def main(args: Array[String]): Unit = {

    //获取配置文件
    val conf = new SparkConf().setMaster("local[*]").setAppName("CdnStatics")

    val sc = new SparkContext(conf)

    //读取配置文件
    val rdd = sc.textFile("/Users/luomingkui/workspace/spark/sparkDemo/sparkCore/sparkcore_cdn/src/main/resources/cdn.txt")

    //rdd.randomSplit(" ")


  }


  /**
    * 计算每一个IP的访问次数   
    * @param input
    */
  def ipStatics(input: Array[String]): Unit = {

    //分割



    //创建最小粒度

    //计算每一个Ip的访问次数（聚合）

  }


  def viewStatics(data: Array[String]): Unit = {

    //1。分割

    //2。构建最小粒度

    //3。过滤出所有的视频访问次数

    //4。构建最小粒度


    //计算每一个Ip的访问次数（聚合）

  }

}
