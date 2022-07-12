package com.github.ValRCS

import CassandraExample.{cassandraExample, getResults, setResults}

object Day14CassandraClient extends App {
    println("Testing Cassandra")

  val host = "cassandra-2fc8be97-valdis-9619.aivencloud.com" //your url will be slightly different
  val port = 22480 //port might also be a different
  val username = "avnadmin" //most likely the same
  val password = "getyourownpassword" //FIXME get from enviroment, DO NOT commit real credentials to git!!
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
}
