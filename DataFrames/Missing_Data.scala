// Start a simple Spark Session
import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

// Grab small dataset with some missing data
val df = spark.read.option("header","true").option("inferSchema","true").csv("ContainsNull.csv")

// df.printSchema()
//
// df.show()

// df.na.drop().show()

// df.na.drop(2).show()

// df.na.fill(100).show()

// df.na.fill("Missing Name").show()

// df.na.fill("New Name", Array("Name")).show()


val df2 = df.na.fill("New Name", Array("Name"))
df2.na.fill(200, Array("Sales")).show()
