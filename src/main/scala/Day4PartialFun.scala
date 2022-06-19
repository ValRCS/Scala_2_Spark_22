package com.github.ValRCS

//https://alvinalexander.com/scala/how-to-define-use-partial-functions-in-scala-syntax-examples/
//A partial function is a function that does not provide an answer for every possible input value it can be given.
// It provides an answer only for a subset of possible data, and defines the data it can handle.
// In Scala, a partial function can also be queried to determine if it can handle a particular value.
object Day4PartialFun extends App {
  println("Exploring Partial Functions")
  println(Day4ObjectApply.x) //should be 1
  Day4ObjectApply.x += 5
  println(Day4ObjectApply.x) //should be 6
  //we would like to call Day4ObjectApply similar how we would call a method inside it
  //Day4ObjectApply(5) to add 5
  val newVal = Day4ObjectApply(10) //so this calls apply
  val fromApply = Day4ObjectApply.apply(10) //same as above line
  println(Day4ObjectApply.x) //should be 6
  println(newVal) //this should be 16
  println(fromApply) //also 16
  Day4ObjectApply.x += 100 //internal state x is now 106
  println(Day4ObjectApply(200)) //should print 306,
  println(Day4ObjectApply.x) // x is still 106

}
