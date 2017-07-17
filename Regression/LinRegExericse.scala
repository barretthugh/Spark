// import LinearRegression
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.LinearRegression

// operation: user the following code below to set the error report
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

//start a simple spark session
import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

//user spark to read in the Ecommerce customers csv file
val data = spark.read.option("header","true").option("inferSchema","true").format("csv").load("CleanEcommerce.csv")

// print the scheam of the dataframe
data.printSchema

// print out the example Row
data.head(2)
// various ways to do this , just choose whichever way you prefer
val colnames = data.columns
val firstrow = data.head(1)(0)
for (ind <- Range(1,colnames.length)){
  println(colnames(ind))
  println(firstrow(ind))
  println("\n")
}

////////////////////////////////////////////////
// setting up dataframe for machine learning //
//////////////////////////////////////////////

// a few things we need to do before spark can accept the data
// it needs to be in the form of two columns
// ("label", "features")

// import VectorAssembler and vectors
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

// rename the yearly amount spent column as "label"
// also grab only the numerical columns from the data
// set all of this as a new dataframe called df
val df = (data.select(data("Yearly Amount Spent").as("label"),
  $"Avg Session Length",
  $"Time on App", $"Time on Website",
  $"Length of Membership"))

// An assembler converts the input values to a vector
// A vector is what the ML algorithm reads to train a model

// Sser VectorAssembler to convert the input columns of df
// to a single output column of an array called "features"
// Set the input columns from which we are supposed to read the values
// Call this new object assembler
val assembler = new VectorAssembler().setInputCols(Array("Avg Session Length", "Time on App",
    "Time on Website", "Length of Membership")).setOutputCol("features")


// Use the assembler to a transform our dataframe to the two columns: Label and Features
val output = assembler.transform(df).select($"label",$"features")

// Create a linear Regression Model object
val lr = new LinearRegression()

// Fit the model to the data and call this model lrModel
val lrModel = lr.fit(output)

// Print the coefficients and intercept for linear regression
println(s"Coeff: ${lrModel.coefficients}, intercept: ${lrModel.intercept}")

// Summarize the model over the training  set and print out same metrics
// user the .summary method off your model to create an object
val trainingSummary = lrModel.summary

// Show the residuals, the RMSE, the MSE, and the R^2 Values
trainingSummary.residuals.show()
println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
println(s"MSE: ${trainingSummary.meanSquaredError}")
println(s"R2: ${trainingSummary.r2}")
