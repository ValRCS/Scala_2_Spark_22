ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "Scala_2_Spark_22",
    idePackagePrefix := Some("com.github.ValRCS")
  )


//NOTE: Log4J dependency is not fixed in this version
// https://mvnrepository.com/artifact/net.debasishg/redisclient
libraryDependencies += "net.debasishg" %% "redisclient" % "3.42"

// https://mvnrepository.com/artifact/org.mongodb.scala/mongo-scala-driver
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.6.1"