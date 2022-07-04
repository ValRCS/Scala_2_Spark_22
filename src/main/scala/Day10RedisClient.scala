package com.github.ValRCS

import com.redis.RedisClient

object Day10RedisClient extends App {
  println("Testing Redis Client Capability")
  //get port and connection url from your Redis Cloud console configuration tab
  val port = 19034
  val url = "redis-19034.c10.us-east-1-4.ec2.cloud.redislabs.com"
  val dbName = "Scala2SparkCourse22"

  val pw = Some("This should not be public") //best practice would be to load int from enviroment variable TODO show how

  val r = new RedisClient(host=url, port, 0, secret= pw)

}
