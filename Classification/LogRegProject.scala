import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

// Create a Spark Session
val spark = SparkSession.builder().getOrCreate()

// Use Spark to read in the Advertising csv file
val data = spark.read.option("header","true").option("inferSchema","true").format("csv").load("advertising.csv")

// Print the Schema of dataframe
data.printSchema()

// display data

// Print out a sample row of the data

// Rename the Clicked on Ad column to "label"
// Grab the following columns "Daily Time Spent on Site", "Age"," Area Income", "Daily Internet Usage", "Male"
// Create a new column called Hour from the Timestamp containing the Hour of click
val timedata = data.withColumn("Hour",hour(data("Timestamp")))

val logregdata = (timedata.select(data("Clicked on Ad").as("label"),
                  $"Daily Time Spent on Site", $"Age", $"Area Income",
                  $"Daily Internet Usage", $"Hour", $"Male")
                  )

// Import VectorAssembler and Vectors
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

// Create a new VectorAssembler object called assembler ofr the feature
// columns as the input Set the output column to be called features
val assembler = (new VectorAssembler().setInputCols(
                Array("Daily Time Spent on Site", "Age", "Area Income",
                "Daily Internet Usage", "Hour", "Male")).setOutputCol("features")
              )

// User randomSplit to cteate a train test split of 70/30
val Array(training, test) = logregdata.randomSplit(Array(0.7,0.3),seed=12345)


// Set Up the Pipeline

// Import Pipeline
import org.apache.spark.ml.Pipeline
// Create a new LogisticRegression object
val lr = new LogisticRegression()

// Create a new pipeline with the stages
val pipeline = new Pipeline().setStages(Array(assembler,lr))

// Fit the pipeline to training set
val model = pipeline.fit(training)

// Get Results on Test Set with transform
val results = model.transform(test)

// results.printSchema()


// MODEL EVALUATION

// For Metrics and Evaluation import MulticlassMetrics
import org.apache.spark.mllib.evaluation.MulticlassMetrics

// Convert the test results to an RDD using .as and .rdd
val predictionAndLabels = results.select($"prediction",$"label").as[(Double, Double)].rdd

// Instantiate a new MulticlassMetrics object
val metrics = new MulticlassMetrics(predictionAndLabels)

// Print out the Confusion matrix
println("Confusion Matrix")
println(metrics.confusionMatrix)
println("\n")
println("Accuracy")
println(metrics.accuracy)
