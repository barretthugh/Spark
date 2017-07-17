import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}

import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

// Read data
val data= spark.read.option("header","true").option("inferSchema","true").format("csv").load("../Regression/CleanUSAHousing.csv")

// data.printSchema

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

val df = data.select(data("Price").as("label"),
  $"Avg Area Income", $"Avg Area House Age",
  $"Avg Area Number of Rooms",
  $"Avg Area Number of Bedrooms", $"Area Population")

// df.printSchema

val assembler = new VectorAssembler().setInputCols(
  Array("Avg Area Income", "Avg Area House Age",
  "Avg Area Number of Rooms",
  "Avg Area Number of Bedrooms", "Area Population")
).setOutputCol("features")


val output = assembler.transform(df).select($"label",$"features")


// Training and Test data
val Array(training, test) = output.select("label","features").randomSplit(Array(0.7,0.3),seed=12345)

// model
val lr = new LinearRegression()

// Parameter Grid Builder
// val paramGrid = new ParamGridBuilder().build()
// val paramGrid = new ParamGridBuilder().addGrid(lr.regParam,Array(100000000,0.001)).build()

val paramGrid = new ParamGridBuilder().addGrid(lr.regParam,Array(100000,0.1)).build()

// Train split (holdout)
val trainvalsplit = (new TrainValidationSplit()
                      .setEstimator(lr)
                      .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
                      .setEstimatorParamMaps(paramGrid)
                      .setTrainRatio(0.8))

val model = trainvalsplit.fit(training)

model.transform(test).select("features","label","prediction").show()

model.validationMetrics
