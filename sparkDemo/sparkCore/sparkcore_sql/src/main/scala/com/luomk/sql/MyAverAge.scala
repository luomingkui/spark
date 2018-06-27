package com.luomk.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * @author luomingkui
  * @date 2018/6/16 上午10:01
  * @desc
  */
class MyAverAge extends UserDefinedAggregateFunction{

  //聚合函数输入的数据类型
  override def inputSchema: StructType = StructType(StructField("salary",LongType)::Nil)

  //小范围聚合临时变量的类型
  override def bufferSchema: StructType = StructType(StructField("sum",LongType)::StructField("count",LongType)::Nil)

  //返回值得类型
  override def dataType: DataType = DoubleType

  //幂等性
  override def deterministic: Boolean = true

  //初始化你的数据结构
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0L
  }

  //每一个分区去更新数据结构
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  //将所有分区的数据合并
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  //计算值
  override def evaluate(buffer: Row): Double = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }

}



object Test{

  def main(args: Array[String]): Unit = {

    val conf  = new SparkConf().setAppName("sql").setMaster("local[*]")

    val spark = SparkSession.builder().config(conf).getOrCreate()

    val sc = spark.sparkContext

    val df = spark.read.json("/Users/luomingkui/workspace/spark/sparkDemo/sparkCore/sparkcore_sql/src/main/resources/employees.json")

    //注意，导入隐式转换，
    import spark.implicits._

    //展示整个表
    df.show()

    //展示整个表的Scheam
    df.printSchema()

    //DSL风格查询
    df.filter($"salary" > 3300).show

    //SQL风格

    //注册一个表名
    df.createOrReplaceTempView("employee")

    //查询
    spark.sql("select * from employee where salary > 3300").show()

    spark.close()
  }

}