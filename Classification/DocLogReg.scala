import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

object  LogisticRegressionWithElasticNetExample{
  def main(): Unit = {
    val spark = SparkSession
      .builder
      .appName("LogisticRegressionWithElasticNetExample")
      .getOrCreate()

    val training = spark.read.format("libsvm").load("sample_libsvm_data.txt")
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val lrModel = lr.fit(training)

    println(s"Cofficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

    spark.stop()
  }
}
