package com.github.ValRCS

import org.neo4j.driver.Values.parameters
import org.neo4j.driver.{AuthTokens, Config, GraphDatabase}

import scala.collection.mutable.ArrayBuffer

case class Movie(id: Int=0, title: String, released: Int, tagline: String)

object Day16Neo4JConnection extends App {
  println("Let's connect to our Neo4J database!")

  val noSSL = Config.builder().build() //so standard default configuration without SSL connection
  val pw = scala.util.Properties.envOrElse("NEO4J_PASSWORD", "")
  //TODO get rest of config from enviroment
  val user = "neo4j"
  val uri = "neo4j+s://e99d3739.databases.neo4j.io"
  val db = "Movies"


  val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pw), noSSL) // <p
  println("Opening Session")
  val session = driver.session

  val cypherQuery = "MATCH (m:Movie) " + "RETURN m as movie, id(m) as id"
  val result = session.run(cypherQuery, parameters())

  //TODO read result(s)
//  val arrayBuffer = ArrayBuffer[String]()
//  while (result.hasNext) {
//    val record = result.next //we know this will not fail because we just checked with hasNext
//    val movie = record.get("movie")
//    arrayBuffer += s"${movie.get("title")} - ${movie.get("released")} : ${movie.get("tagline")}"
//  }
    val resultBuffer = ArrayBuffer[Movie]()
    while (result.hasNext) {
      val record = result.next //represent one entry/ similar to row in SQL
      val movie = record.get("movie")
      resultBuffer += Movie(
//        record.get("id").asInt(), //we get the id from the actual record/row
        movie.asNode().id().toInt, //alternative to previous line
        movie.get("title").asString(),
        movie.get("released").asInt(),
        movie.get("tagline").asString())
//      println(record.fields()) //here movie: node<0>, id: 0
//      println(movie.asNode().id()) //this would also give you id of the movie
    }

  println("Closing Session")
  session.close() //important to close the session

  val movies = resultBuffer.toArray
  println(s"We got ${movies.length} movies!")
  movies.take(5).foreach(println)

  println("Earliest 10 releases")
  val sortedByYear = movies.sortBy(_.released) //Ascending by default
  sortedByYear.take(10).foreach(println)

  //TODO set up your own Movies database on https://neo4j.com/cloud/platform/aura-graph-database/?ref=neo4j-home-hero
  //TODO get all movies as case classes - with all attributes - title, released,tagline and id - i think that is it
  //TODO print earliest 10 movies in this database
}
