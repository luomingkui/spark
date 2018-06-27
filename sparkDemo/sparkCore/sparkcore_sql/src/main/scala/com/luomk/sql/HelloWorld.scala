package com.luomk.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * @author luomingkui
  * @date 2018/6/16 上午12:59
  * @desc
  */
object HelloWord {

  def main(args: Array[String]): Unit = {

    val conf  = new SparkConf().setAppName("sql").setMaster("local[*]")

    val spark = SparkSession.builder().config(conf).getOrCreate()


    val df = spark.read.json("/Users/luomingkui/workspace/spark/sparkDemo/sparkCore/sparkcore_sql/src/main/resources/employees.json")

    //注意，导入隐式转换，
    import spark.implicits._

    println("===展示整个表===")
    df.show()

    println("===展示整个表的Scheam===")
    df.printSchema()

    println("===DSL风格查询===")
    df.filter($"salary" > 3300).show

    //SQL风格

    //注册一个表名
    df.createOrReplaceTempView("employee")

    println("===条件查询===")
    spark.sql("select * from employee where salary > 3300").show()

    spark.close()
  }

}