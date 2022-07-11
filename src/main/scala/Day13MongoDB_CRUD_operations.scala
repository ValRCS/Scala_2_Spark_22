package com.github.ValRCS

import org.mongodb.scala.model.Filters //we use this for lt, which we use Filters.lt for
import org.mongodb.scala.model.Filters.{and, equal, gte}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import java.lang.Thread.sleep
import scala.collection.mutable.ArrayBuffer

object Day13MongoDB_CRUD_operations extends App {
   println("Exploring CRUD operations in MongoDB")

  val userName = scala.util.Properties.envOrElse("MONGOUSER", "nosuchuser") //TODO read from system enviroment
  val pw = scala.util.Properties.envOrElse("MONGOPW", "na")//TODO read from system enviroment do not commit real passwords and usernames to git
  println(s"Connecting with user $userName")

  //you need to use your own server uri as well
  val uri: String = s"mongodb+srv://$userName:$pw@cluster0.qabwl.mongodb.net/?retryWrites=true&w=majority"
  //here we connect to the MongoDB cluster
  val client: MongoClient = MongoClient(uri)

  //connecting to the actual database - single cluster could have many databases
  val db: MongoDatabase = client.getDatabase("sample_restaurants")

  val collectionName = "restaurants"

  //we connect to the collection (which is roughly similar to table in SQL)
  val collection = db.getCollection(collectionName)

  val resultsBuffer = ArrayBuffer[Document]()




//  val allRestaurants = collection.find() //find() is similar to SQL SELECT * from restaurants just in MongoDB syntax
//  val allRestaurants = collection.find(equal("borough","Staten Island"))
//  val allRestaurants = collection.find(and(gte("stars", 2), Filters.lt("stars", 5), equal("categories", "Bakery")))
//    val allRestaurants = collection.find(gte("address.building", "3000")) //greather lexicographically
    val allRestaurants = collection.find(Filters.regex("name", ".*Kosher.*"))
    .subscribe(
      (doc: Document) => {
        resultsBuffer += doc //so each document(row of JSON) will be added to our buffer
      },
      (e: Throwable) => println(s"Query error: $e"),
      //this is what we can do after the query is finished
      afterQuerySuccess //NOTICE in functional style I do not call the function here I just tell my subscription WHAT to call
    )
  //this line should run before our closing line
  println("Query is still Running - Data is not guaranteed to be ready")
//  println(s"Buffer length is ${resultsBuffer.length}")
//  sleep(2000)
//  println(s"Buffer length is ${resultsBuffer.length}")
  //looks like data is returned in one big swell swoop so buffer is 0 then very quickly it fills up


  def afterQuerySuccess():Unit = {
    println("Closing after last query")
    //so idea is to close after last query is complete
    val allRestaurantDocs = resultsBuffer.toArray
    println(s"We got ${allRestaurantDocs.length} restaurants total")
    println("First restaurant")
    println(allRestaurantDocs.head.toJson())
    val savePath = "src/resources/json/restaurants.json"
    val restaurantJSON = allRestaurantDocs.map(_.toJson()) //so we convert/map all documents to JSON strings
    Util.saveLines(savePath, restaurantJSON) //so we extract ALL of the collection and save it for later use
    client.close()
  }

}
