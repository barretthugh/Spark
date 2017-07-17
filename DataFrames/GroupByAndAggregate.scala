import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

val df = spark.read.option("header","true").option("inferSchema","true").csv("Sales.csv")

// df.printSchema()
//
// df.show()

// df.groupBy("Company").mean().show()

// df.groupBy("Company").count().show()

// df.groupBy("Company").max().show()
// df.groupBy("Company").min().show()
// df.groupBy("Company").sum().show()

// df.select(sum("Sales")).show()

// df.select(countDistinct("Sales")).show() //approxCountDistinct
// df.select(sumDistinct("Sales")).show()
// df.select(variance("Sales")).show()
// df.select(stddev("Sales")).show() // avg, max, min sum stddev
// df.select(collect_set("Sales")).show()

df.orderBy($"Sales".desc).show()
