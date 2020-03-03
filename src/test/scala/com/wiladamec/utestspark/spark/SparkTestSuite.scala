package com.wiladamec.utestspark.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import utest.TestSuite

abstract class SparkTestSuite extends TestSuite {

  def testSparkConf(shufflePartitions: Int = 1): SparkConf = {

    new SparkConf()
      .set("spark.master", "local")
      .set("spark.sql.shuffle.partitions", shufflePartitions.toString)
  }

  def testWithSpark[A](sparkConf: SparkConf)
                      (f: SparkSession => A): A = {

    val spark = SparkSession
      .builder
      .config(sparkConf)
      .getOrCreate

    try {
      f(spark)
    }
    finally {
      spark.stop()
      System.clearProperty("spark.master.port")
    }
  }

  def testWithSpark[A](shufflePartitions: Int = 1)
                      (f: SparkSession => A): A = {

    val sparkConf = testSparkConf(shufflePartitions)

    testWithSpark(sparkConf)(f)
  }
}