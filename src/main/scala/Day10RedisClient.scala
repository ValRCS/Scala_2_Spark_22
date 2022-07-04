package com.github.ValRCS

import com.redis.RedisClient //

object Day10RedisClient extends App {
  println("Testing Redis Client Capability")
  //get port and connection url from your Redis Cloud console configuration tab
  val port = 19034
//  val url = "redis-19034.c10.us-east-1-4.ec2.cloud.redislabs.com"
  val url = scala.util.Properties.envOrElse("REDISHOST","nodatabaseurl")
  println(s"Will connect to Redis database at: $url")
  val dbName = "Scala2SparkCourse22"

//  val pw = Some("This should not be public") //best practice would be to load int from enviroment variable TODO show how
  val pw = Some(scala.util.Properties.envOrElse("REDISPW", "nopassword")) //check system enviroment for REDISPW key
  println(s"My password is: $pw") //you do not generally print your pw on the screen :)
  //you can see any other system or user variables
//  println(scala.util.Properties.envOrElse("GoLand", "nosuchvalue"))

  val r = new RedisClient(host=url, port, 0, secret= pw)

  r.set("myname", "Valdis")
  r.incr(key = "mycount") //so either initialize value to 1 or increment by 1
  //we print values directly from database
  println(r.get("myname"))
  //or save into a value/variable
  val myCounter = r.get("mycount")
  val actualCounter = myCounter.getOrElse("0").toInt
  println(s"My counter is at $myCounter -> $actualCounter")

  //TODO connect to your own database not mine :)
  //maybe test a few more commands incrBy or something else ?
  val number = r.get("number") //gets you Option[String] or None
  println(number) // will be None the first time
  val resultOfDecrby = r.decrby("number", 2) //no key "number" will be decremented 2 from 0 the first time
  //then afterwards from the current value
  println(resultOfDecrby, r.get("number")) //Some(-2) the first time then Some(-4), -6 etc

 //let's get all the present keys
  val keys = r.keys().getOrElse(List[Some[String]]()) //so if there are no keys give me empty List of Strings

  println("My keys are")
//  keys.foreach(key => println(s"Key $key type is ${key.getClass} value: ${r.get(key.getOrElse(""))}")) //only works on primitives
  keys.foreach(key => println(s"Key $key type is ${key.getClass}"))

  r.lpush("friends", "Alice")
  r.lpush("friends", "Bob")
  r.rpush("friends", "Carol")
  r.del("friend") //deleting unused key //no error on no key deletion

  val friends = r.lrange("friends", 0, 10).getOrElse(List[Some[String]]())
  println("All my friends")
  friends.foreach(println)
  friends.foreach(friend => println(s"Friend: ${friend.getOrElse("no friend")}"))

  //Set Example
  val addCount = r.sadd("superpowers", "flight", "x-ray vision", "freezing")
  println(s"Added to set ${addCount.getOrElse(0L)}")
  println(r.smembers("superpowers"))
}
