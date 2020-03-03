lazy val utestSparkExample = (project in file("."))
  .settings(
    name := "utest-spark-example",
    organization := "com.wiladamec",
    version := "0.1",

    // Scala 2.12.10 is the latest Scala version supported by Spark
    scalaVersion := "2.12.10",

    testFrameworks += new TestFramework("utest.runner.Framework"),

    // Disable parallel execution of tests so that only one SparkContext will run at a time
    parallelExecution in Test := false,

    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "2.4.5" % "provided",
      "com.lihaoyi" %% "utest" % "0.7.2" % "test"
    )
  )
