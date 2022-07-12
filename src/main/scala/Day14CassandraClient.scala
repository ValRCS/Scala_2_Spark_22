package com.github.ValRCS

import CassandraExample.{cassandraExample, getResults, getClusterSession, runQuery, setResults}

import scala.collection.mutable.ArrayBuffer

case class Message(id: Int, msg: String)

object Day14CassandraClient extends App {
    println("Testing Cassandra")

  val host = "cassandra-2fc8be97-valdis-9619.aivencloud.com" //your url will be slightly different
  val port = 22480 //port might also be a different
  val username = "avnadmin" //most likely the same
  val password = scala.util.Properties.envOrElse("CASSANDRA_PW", "")
  //  val caPath = "C:\\certs\\"
  val caPath = "./src/resources/certs/ca.pem" //you need to download your own cert, do not commit to GIT!!

  cassandraExample(host=host,port=port,username=username,password=password, caPath=caPath)
  println("Lets hold our fingers crossed")
  setResults(host=host,
    port=port,
    username=username,
    password=password,
    caPath=caPath,
    keyspace = "example_keyspace",
    id = 9000,
    message = "Hello from Space on Jul 12ndd")

  val query = "SELECT id, message FROM example_java"
  val results = getResults(host=host,port=port,username=username,password=password, caPath=caPath, keyspace = "example_keyspace", query = query)
  results.foreach(println)

  //we unpack the tuple from getClusterSession,because we do not want to use mytuple._1 and mytuple._2
  val (cluster, session) = getClusterSession(host=host,port=port,username=username,password=password, caPath=caPath)
  val rSet = runQuery(session, "example_keyspace", "SELECT id, message FROM example_java" )
  val resultBuffer = ArrayBuffer[Message]()
  rSet.forEach(row => resultBuffer += Message(row.getInt("id"), row.getString("message")))
  val resultArray = resultBuffer.toArray
  println(resultArray.mkString(","))
//  runQuery(session, "example_keyspace", "ALTER TABLE example_java ADD created timestamp")
  val newResultSet = runQuery(session, "example_keyspace", "SELECT * FROM example_java" )
  newResultSet.forEach(row => println(s"$row"))
  //  rSet.forEach(row => println(s"ID: ${row.getInt("id")}, message: ${row.getString("message")}"))
  cluster.close() //we cleanup the connection, otherwise our program continues to run
}
