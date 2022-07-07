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

  //TODO 3 more hackers with their scores/birthyear ( you can use your own or use the ones from redis.io example
  //TODO get all hackers born after 1960 -
 //use zrangebyscore method
  r.zadd("hackers", 1949, "Anita Borg")
  r.zadd("hackers", 1965, "Yukihiro Matsumoto")
  r.zadd("hackers", 1916, "Claude Shannon")

  val hackers1960 = r.zrangebyscore("hackers",
    1960,
    true,
    2000,
    true,
    Option(0,100)).getOrElse(List[String]())
  println(hackers1960.mkString(","))

  //you can update the scores at any time
  r.zadd("hackers", 2086, "Claude Shannon")
  println(r.zrangebyscore("hackers",
    1960,
    true,
    2100,
    true,
    Option(0,100)).getOrElse(List[String]()).mkString(","))
  //turns outs zrangebyscore is considered depreceated meaning it will have to be replaced by different query
  //zrange using byscore option
  //https://redis.io/commands/zrange/
  //TODO see if it is implemented in the client val hackers1970 = r.zrange("hackers",0, -1)

  //TODO create a new hash key with at least 5 fields with corresponding values
  //TODO retrieve 3 of those values - you can use hget
  // alternative would be r.hmget

  r.hmset("user:33", Array(("name", "Nikita"), ("likes", "french fries"), ("color", "black"), ("age", 23)))
  val newName = r.hget("user:33", "name").getOrElse("")
  println(newName)
  val newAge = r.hget("user:33", "age").getOrElse("0").toInt
  println(newAge)
  val favouriteFood = r.hget("user:33", "likes").getOrElse("")
  println(favouriteFood)

  //idea behind hyperLogLog is to use very tiny amountof memory
  //to allow counting of unique items in some collection
  //by using pfadd we do not actually store the real data there
  //all we get in return is ability to count unique items quickly and cheaply
  r.pfadd("events:day1", "a", "b", "c", "d", "e", "f", "e", "e")
  r.pfadd("events:day2", "a", "b", "c", "c")
  val eventsCountDay1 = r.pfcount("events:day1").getOrElse(0)
  val eventsCountDay2 = r.pfcount("events:day2").getOrElse(0)
  println(s"There were approximately $eventsCountDay1 unique events on Day 1")
  println(s"There were approximately $eventsCountDay2 unique events on Day 2")
  r.pfmerge("events:allDays", "events:day1", "events:day2")

  val eventsAllDays = r.pfcount("events:allDays")
  println(s"There were approximately $eventsAllDays unique events on all days")

  //streams - data is arriving non-stop it is not finished
  //https://redis.io/docs/manual/data-types/streams/


}
