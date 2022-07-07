package com.github.ValRCS

import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import java.lang.Thread.sleep

object Day12MongoDB extends App {
  println("Testing MongoDB")

  val userName = "getYourOwnUsername" //TODO read from system enviroment
  val pw = "yourOwnPassword" //TODO read from system enviroment do not commit real passwords and usernames to git

  //you need to use your own server uri as well
  val uri: String = s"mongodb+srv://$userName:$pw@cluster0.qabwl.mongodb.net/?retryWrites=true&w=majority"
  val client: MongoClient = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase("sample_restaurants")

  val collectionName = "restaurants"
  val collection = db.getCollection(collectionName)

  //so SQL analog would be  to SELECT * FROM restaurants WHERE name = 'Carvel Ice Cream'
  val carverIceCream = collection.find(equal("name", "Carvel Ice Cream"))
    .subscribe((doc: Document) => println(doc.toJson()),
      (e: Throwable) => println(s"Query error: $e"),
      () => println("Runs after query is complete")
    )

  //TODO set up your own MongoDB Cloud connection and cluster
  //TODO use European servers
  //TODO your cluster should come with sample databases including sample_restaurants
  //TODO print all restaurants on Broadway street

  sleep(5000) //we will wait for the query to go through
  //we need to close the client otherwise the program will keep runing
  client.close()

}
