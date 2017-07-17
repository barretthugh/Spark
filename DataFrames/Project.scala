import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

// Load the Netflix Stock CSV File, have Spark infer the data types.
val df = spark.read.option("header","true").option("inferSchema","true").csv("Netflix_2011_2016.csv")

// df.columns

// (df.filter($"High">500).count()*1.0/df.count())*100
//
// val yeardf = df.withColumn("Year",year(df("Date")))
// val yearmax = yeardf.select($"year",$"High").groupBy("Year").max()
//
// yearmax.select($"Year",$"max(High)").show()
// var result = yearmax.select($"Year",$"max(High)")
//
// result.orderBy("Year").show()

val monthdf = df.withColumn("Month",month(df("Date")))
val monthavgs = monthdf.select($"Month",$"Close").groupBy("Month").mean()

// monthavgs.select($"Month",$"avg(Close)").show()
monthavgs.select($"Month",$"avg(Close)").orderBy("Month").show()
