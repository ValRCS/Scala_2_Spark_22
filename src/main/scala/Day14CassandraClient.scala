package com.github.ValRCS

import CassandraExample.{cassandraExample, getResults, getSession, runQuery, setResults}

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

  val session = getSession(host=host,port=port,username=username,password=password, caPath=caPath)
  val rSet = runQuery(session, "example_keyspace", "SELECT id, message FROM example_keyspace" )
  rSet.forEach(row => println(s"ID: ${row.getInt("id")}, message: ${row.getString("message")}"))
}
