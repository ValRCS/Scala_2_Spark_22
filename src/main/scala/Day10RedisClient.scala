package com.github.ValRCS

import com.redis.RedisClient

object Day10RedisClient extends App {
  println("Testing Redis Client Capability")
  //get port and connection url from your Redis Cloud console configuration tab
  val port = 19034
  val url = "redis-19034.c10.us-east-1-4.ec2.cloud.redislabs.com"
  val dbName = "Scala2SparkCourse22"

//  val pw = Some("This should not be public") //best practice would be to load int from enviroment variable TODO show how
  val pw = Some("do not put real password into git!!") //best practice would be to load int from enviroment variable TODO show how

  val r = new RedisClient(host=url, port, 0, secret= pw)

  r.set("myname", "Valdis")
  r.incr(key = "mycount") //so either initialie value to 1 or increment by 1
  //we print values directly from database
  println(r.get("myname"))
  //or save into a value/variable
  val myCounter = r.get("mycount")
  println(s"My counter is at $myCounter")

  //TODO connect to your own database not mine :)
  //maybe test a few more commands incrBy or something else ?


}
