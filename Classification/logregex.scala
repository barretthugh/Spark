import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

val spark = SparkSession.builder().getOrCreate()

val data = spark.read.option("header","true").option("inferSchema","true").format("csv").load("titanic.csv")

data.printSchema()

val colnames = data.columns
val firstrow = data.head(1)(0)
println("\n")
println("Example Data Row")
for (ind <- Range(1,colnames.length)){
  println(colnames(ind))
  println(firstrow(ind))
  println("\n")
}

val logregdataall = (data.select(data("Survived").as("label"),
  $"Pclass", $"Name", $"Sex", $"Age", $"SibSp", $"Parch", $"Fare", $"Embarked"))

val logregdata = logregdataall.na.drop()

import org.apache.spark.ml.feature.{VectorAssembler,StringIndexer,VectorIndexer,OneHotEncoder}
import org.apache.spark.ml.linalg.Vectors

// Converting Strings into numerical values
val genderIndexer = new StringIndexer().setInputCol("Sex").setOutputCol("SexIndex")
val embarkIndexer = new StringIndexer().setInputCol("Embarked").setOutputCol("EmberkIndex")

// COnverting Numberical Values into One Hot Encoding 0 or 1
val genderEncoder = new OneHotEncoder().setInputCol("SexIndex").setOutputCol("SexVec")
val embarkEncoder = new OneHotEncoder().setInputCol("EmberkIndex").setOutputCol("EmbarkVec")

// (label, features)
val assembler = (new VectorAssembler()
                .setInputCols(Array("Pclass", "SexVec", "Age", "SibSp",
                  "Parch", "Fare", "EmbarkVec")).setOutputCol("features"))

val Array(training, test) = logregdata.randomSplit(Array(0.7,0.3), seed=12345)

import org.apache.spark.ml.Pipeline

val lr = new LogisticRegression()

val pipeline = new Pipeline().setStages(Array(genderIndexer, embarkIndexer, genderEncoder, embarkEncoder, assembler, lr))

val model = pipeline.fit(training)

val result = model.transform(test)

/////////////////////////////////////
// MODEL EVALUATION
////////////////////////////////////
import org.apache.spark.mllib.evaluation.MulticlassMetrics

val predictionAndLabels = result.select($"prediction", $"label").as[(Double, Double)].rdd

val metrics = new MulticlassMetrics(predictionAndLabels)

println("Confusion matrix: ")
println(metrics.confusionMatrix)
println("\n")
println("Accuracy")
println(metrics.accuracy)
