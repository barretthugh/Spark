import org.apache.spark.sql.SparkSession

import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

val spark = SparkSession.builder().getOrCreate()

import org.apache.spark.ml.clustering.KMeans

val dataset = spark.read.option("header","true").option("inferSchema","true").csv("Wholesale customers data.csv")

val feature_data = dataset.select($"Region", $"Fresh", $"Milk", $"Grocery", $"Frozen", $"Detergents_Paper", $"Delicassen")


import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

val assember = new VectorAssembler().setInputCols(Array("Fresh", "Milk", "Grocery", "Frozen", "Detergents_Paper", "Delicassen")).setOutputCol("features")

val training_data = assember.transform(feature_data).select("features")

val kmeans = new KMeans().setK(3)

val model = kmeans.fit(training_data)

val WSSSE = model.computeCost(training_data)

println(s"The Within Set Sum of Squared Errors was $WSSSE")
