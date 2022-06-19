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

  def divideFun(n: Int): Int = 9000 / n
  println(divideFun(5)) //should 1800
  // println(divideFun(0)) //should throw an exception //we could use try catch
  //as discussed previously in https://docs.scala-lang.org/overviews/scala-book/try-catch-finally.html

  //we can define functions using anonymous function syntax
  val divideFunToo = (n: Int) => 9000 / n //so i said value divideFunToo actually points to a function which we just defined anonymously
  println(divideFunToo(5))
//  println(divideFunToo(0)) //same problem with exception

  //so PartialFunction needs to define two methods
  //apply
  //isDefinedAt - which returns boolean depending on whether we accept incoming function argument
  val dividePartial = new PartialFunction[Int, Int] {
    def apply(n: Int): Int = 9000 / n //so what to do
    def isDefinedAt(n: Int): Boolean = n != 0 //what is not acceptable
  }

  println(dividePartial(5)) //should be 1800 again
//  println(dividePartial(0)) //again an exception, so what did we gain ?
  //we gain abstraction of our error checking
  val arg = 10
  if (dividePartial.isDefinedAt(arg)) {
    println(s"Result is ${dividePartial(arg)}")
  } else {
    println(s"Sorry $arg is not defined for dividePartial partial function")
  }

  //you can run into partial function if you have a pattern matching where you do not cover the default case
 //so apply and isDefinedAt are free here
  val divide2: PartialFunction[Int, Int] = {
    case x: Int if x != 0 => 9000 / x
  }
  println(divide2(300)) //should print 30
  println(divide2.isDefinedAt(0))
  println(divide2.isDefinedAt(1))


  //we can combine multiple partial Functions to process some data

  // converts 1 to "one", etc., up to 5
  val convert1to5 = new PartialFunction[Int, String] {
    val numbers: Array[String] = Array("one", "two", "three", "four", "five")
    def apply(i: Int): String = numbers(i-1)
    def isDefinedAt(i: Int) = i > 0 && i < 6
  }

  // converts 6 to "six", etc., up to 10
  val convert6to10 = new PartialFunction[Int, String] {
    val numbers: Array[String] = Array("six", "seven", "eight", "nine", "ten")
    def apply(i: Int): String = numbers(i-6) //here you would have to be careful with the bounds
    def isDefinedAt(i: Int) = i > 5 && i < 11
  }

  //they each handle 5 numbers each, but we can combine them!
  //in a functional way
  val handle1to10 = convert1to5 orElse convert6to10 //so we combine these partial functions to create a new Partial Function!
  println(handle1to10(3))
  println(handle1to10(8))
  //println(handle1to10(34232)) //still an exception ArrayIndex out of Bound so you would need to check is defined
  println(handle1to10.isDefinedAt(34232)) //naturally false

  //checking this isDefinedAt can get old pretty fast but we have something cool left in store
  //we have collect to operate as a map + filter at once

  val someNumbers = (-6 to 15).toArray

  //so collect wants a partial function as a parameter and will give values in a new collection wherever the arguments are defined
  val numberWords = someNumbers.collect(handle1to10)

  println(numberWords.mkString(","))

  val int2String: PartialFunction[Int, String] = {
    case 1 => "One"
    case 9 => "Nine"
    case -3 => "Minus three"
    case 15 => "Fifteen"
      //notice we have no case _ => "default value defined"
  }

  //partial function with everything defined...
  val int2StringDefault: PartialFunction[Int, String] = {
    case 1 => "One"
    case 9 => "Nine"
    case -3 => "Minus three"
    case 15 => "Fifteen"
    case _ => "default val"
  }

  val alsoNumWords = someNumbers.collect(int2String)
  println(alsoNumWords.mkString(","))

  //so collected filtered only those which had a definition and then mapped using int2String partial function

  val allNumWords = someNumbers.collect(int2StringDefault)

  println(allNumWords.mkString(","))

  //so this is a functional programming approach to processing some subset of data without using ifs explicitly
}
