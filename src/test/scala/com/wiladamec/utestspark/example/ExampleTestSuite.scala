package com.wiladamec.utestspark.example

import utest._

import com.wiladamec.utestspark.spark.SparkTestSuite

object ExampleTestSuite extends SparkTestSuite {

  val tests = Tests {

    test("parallelizeAndCollect") {

      testWithSpark() { spark =>

        val numbers = spark.sparkContext.parallelize(1 to 8, 8)

        val collectedNumbers = numbers.collect

        assert(collectedNumbers.length == 8)
      }
    }

    test("workOnDataFrame") {

      testWithSpark() { spark =>

        import spark.implicits._
        import org.apache.spark.sql.functions.avg

        val fruitPrices = Seq(
          ("banana", "Kroger", 2.00),
          ("banana", "Whole Foods", 8.00),
          ("apple", "Walmart", 1.00),
          ("apple", "Kroger", 2.00),
          ("apple", "Whole Foods", 9.00),
          ("pear", "Food Giant", 3.00),
          ("pear", "Whole Foods", 25.00)
        )
          .toDF("fruit", "store", "price")

        val averageFruitPrices = fruitPrices
          .groupBy('fruit)
          .agg(avg('price) as "avgPrice")
          .sort('fruit)
          .as[(String, Double)]

        val expected = Seq(
          ("apple", 4.00),
          ("banana", 5.00),
          ("pear", 14.00)
        )

        assert(averageFruitPrices.collect.toSeq == expected)
      }
    }
  }
}