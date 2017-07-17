import org.apache.spark.sql.SparkSession

var spark = SparkSession.builder().getOrCreate()

// val df = spark.read.csv("CitiGroup2006_2008")

// val df = spark.read.option("header","true").csv("CitiGroup2006_2008")

val df = spark.read.option("header","true").option("inferSchema","true").csv("CitiGroup2006_2008")

// df.head(5)

// for (row <- df.head(5)){
//   println(row)
// }

// df.columns

// df.describe().show()

// create column
val df2 = df.withColumn("HighPlusLow", df("High")+df("Low"))

// df2.printSchema()

df2.select(df2("HighPlusLow").as("HPL"), df2("Close")).show()
