package com.github.ValRCS

import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import java.lang.Thread.sleep
import scala.collection.mutable.ArrayBuffer

object Day12MongoDB extends App {
  println("Testing MongoDB")

  val userName = scala.util.Properties.envOrElse("MONGOUSER", "nosuchuser") //TODO read from system enviroment
  val pw = scala.util.Properties.envOrElse("MONGOPW", "na")//TODO read from system enviroment do not commit real passwords and usernames to git
//  println(userName, pw)

  //you need to use your own server uri as well
  val uri: String = s"mongodb+srv://$userName:$pw@cluster0.qabwl.mongodb.net/?retryWrites=true&w=majority"
  //here we connect to the MongoDB cluster
  val client: MongoClient = MongoClient(uri)

  //connecting to the actual database - single cluster could have many databases
  val db: MongoDatabase = client.getDatabase("sample_restaurants")

  val collectionName = "restaurants"

  //we connect to the collection (which is roughly similar to table in SQL)
  val collection = db.getCollection(collectionName)

  //so SQL analog would be  to SELECT * FROM restaurants WHERE name = 'Carvel Ice Cream'
//  val carverIceCream = collection.find(equal("name", "Carvel Ice Cream")) //this is our query
//    .subscribe((doc: Document) => println(doc.toJson()), //this is what you do with each document (row)
//      (e: Throwable) => println(s"Query error: $e"), //this is what you do if you encounter an error in query
//      () => println("Runs after query is complete") //this runs after query is complete we could put client.close() here btw
//    )

  //TODO set up your own MongoDB Cloud connection and cluster
  //TODO use European servers
  //TODO your cluster should come with sample databases including sample_restaurants
  //TODO print all restaurants on Broadway street

//  val broadwayRestaurants = collection.find(equal("address.street", "Broadway"))
//    .subscribe((doc: Document) => println(doc.toJson()),
//      (e: Throwable) => println(s"Query error: $e"),
//      () => println("Runs after query is complete")
//    )
//  sleep(5000) //we will wait for the query to go through
  //we need to close the client otherwise the program will keep runing

  val resultsBuffer = ArrayBuffer[Document]()

  //combining filters with and - conjunction
  val broadwayRestaurantsManhattanQuery = collection.find(and(equal("address.street", "Broadway"),
    equal("borough", "Manhattan")))
    .subscribe((doc: Document) => {
      println(doc.toJson())
      resultsBuffer += doc
    },
      (e: Throwable) => println(s"Query error: $e"),
      () => {
        println("Closing after last query")
        //so idea is to close after last query is complete
        val broadwayRestaurantsManhattan = resultsBuffer.toArray
        println(s"We got ${broadwayRestaurantsManhattan.length} restaurants on Broadway street in Manhattan")
        println("First restaurant")
        println(broadwayRestaurantsManhattan.head.toJson())
        val firstRestaurant = broadwayRestaurantsManhattan.head //single Document
        println(firstRestaurant.getOrElse("name", "unknown")) //still in BSON wrapper
        println(firstRestaurant.getOrElse("name", "unknown").asString().getValue) //regular Scala String
        println(firstRestaurant.getOrElse("cuisine", default = "NA").asString().getValue)
        println(firstRestaurant.getOrElse("address", default = "").asDocument().toString)
        println(firstRestaurant.getOrElse("address", default = "")
          .asDocument().getString("building").getValue.toInt
    )
        client.close() //so we close upon last client without sleep so called asynchronous programming
      }
    )

    //if we keep going without pause here well we might not have the results just yet
  //we could do something else here not related to our query and our program would not hang

}
