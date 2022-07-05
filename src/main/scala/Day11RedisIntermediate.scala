package com.github.ValRCS

import com.redis.RedisClient

object Day11RedisIntermediate extends App {
  println("Deeper look into Redis commands")
  val port = 19034
  val url = scala.util.Properties.envOrElse("REDISHOST","nodatabaseurl")
  val pw = Some(scala.util.Properties.envOrElse("REDISPW",""))

  val r = new RedisClient(host=url, port, 0, secret= pw)
  r.set("computer:type", "Lenovo")

  //so we either get a List of String Options or we get a blank List of String Options
  val myValues = r.mget("myname", "mycount", "badkey", "number", "favorites:berry").getOrElse(List[Option[String]]()).toArray
  //these are just the values, associate the keys yourself
  for (value <- myValues) {
    println(s"value -> ${value.getOrElse("")}")
  }

  //set multiple values at once
  val msetResults = r.mset(("weather","sunny"),("temperature", 25),("favorites:berry","strawberries")) //notice no restrictions on values
  println(s"Mset worked?: $msetResults")

  //let's get all the present keys
  val keys = r.keys().getOrElse(List[Some[String]]()).map(_.getOrElse(""))
  println("My keys are")
  //  keys.foreach(key => println(s"Key $key type is ${key.getClass} value: ${r.get(key.getOrElse(""))}")) //only works on primitives
  keys.foreach(key => println(s"Key $key type is ${key.getClass}"))

  //so we set key user:42 it will have hash fields name, likes , color withs corresponding values
  r.hmset("user:42",Array(("name","Valdis"),
    ("likes","potatoes"),
    ("color","green"),
    ("parkingTickets", 3)
  ))

  val myName = r.hget("user:42", "name").getOrElse("")
  println(s"My name is $myName")

  var parkingTickets = r.hget("user:42", "parkingTickets").getOrElse("0").toInt
  println(s"$myName has got $parkingTickets parking Tickets")

  r.hincrby("user:42", "parkingTickets", 10)
  parkingTickets = r.hget("user:42", "parkingTickets").getOrElse("0").toInt
  println(s"$myName has got $parkingTickets parking Tickets")

  //sortedSets
  //https://redis.io/docs/manual/data-types/data-types-tutorial/#sorted-sets

  r.zadd("hackers",  1940, "Alan Kay")
  r.zadd("hackers",  1957, "Sophie Wilson")
  r.zadd("hackers" ,1912, "Alan Turing")
  r.zadd("hackers", 1969, "Linus Torvalds")

  val hackers = r.zrange("hackers", 0, -1).getOrElse(List[String]())
  println(hackers.mkString(","))

}
