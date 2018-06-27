package com.luomk.exer

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * @author luomingkui
  * @date 2018/6/16 下午2:06
  * @desc
  * 1.1	计算所有订单中每年的销售单数、销售总额
  * 1.2	计算所有订单每年最大金额订单的销售额
  * 1.3	计算所有订单中每年最畅销货品
  *
  */

case class tbStock(ordernumber: String, locationid: String, dateid: String) extends Serializable

case class tbStockDetail(ordernumber: String, rownum: Int, itemid: String, number: Int, price: Double, amount: Double) extends Serializable

case class tbDate(dateid: String, years: Int, theyear: Int, month: Int, day: Int, weekday: Int, week: Int, quarter: Int, period: Int, halfmonth: Int) extends Serializable

object Practice {

  def main(args: Array[String]): Unit = {


   /* //创建spark配置
    val conf  = new SparkConf().setAppName("Practice").setMaster("local[*]")

    //创建sparksql客户端
    val spark = SparkSession.builder().config(conf).getOrCreate()


    //注意，导入隐式转换，
    import spark.implicits._

    //加载数据到hive中
    //订单表的详细信息
    val tbStockRdd=spark.read.textFile("/Users/luomingkui/workspace/spark/sparkDemo/sparkCore/sparkcore_sql/src/main/resources/tbStock.txt")

    val tbStockDS = tbStockRdd.map(_.split(",")).map(attr => tbStock(attr(0), attr(1), attr(2))).toDS

    insertHive(spark, "tbDate", tbDateDS.toDF)

    // 1.1	计算所有订单中每年的销售单数、销售总额：三个表连接后以count(distinct a.ordernumber)计销售单数，sum(b.amount)计销售总额

    val result1 = spark.sql("SELECT c.theyear, COUNT(DISTINCT a.ordernumber), SUM(b.amount) FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear ORDER BY c.theyear")

    insertMySQL("xq1",result1)*/


  }

}
