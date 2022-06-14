package com.github.ValRCS

import scala.io.StdIn.readLine

object Day2ViewMapFilterReduceExercise extends App {
  println("Ask user for input")
  val number = readLine("Please Enter a number").toInt //you could add error checking if you remember from last class
  println(number)

  val numbers = (0 to number).toArray
  println(s"All numbers: "+numbers.mkString(","))

  //on a large (millions) map filter you should put view first then toArray at the end
  val oddSquares = numbers.map(n => n*n).filter(n => n%2 == 1)
  println(s"Only odd squares of numbers " + oddSquares.mkString(","))

  //here we supply reduce function inline(inside our parenthesis)
  val productOfOddSquares = numbers.view.map(n => n*n).filter(n => n%2 == 1).reduce(_*_)
  //same as above
  println(numbers.view.map(n => n*n).filter(n => n%2 == 1).reduce((acc,v) => acc*v))
  println(s"Product of odd squares is  $productOfOddSquares")

  def mult(a:Int, b:Int):Int = a*b

  //we can pass already defined functions to reduce as long as it has two parameters
  println(numbers.view.map(n => n*n).filter(n => n%2 == 1).reduce(mult))

  def multLong(a:Long, b:Long):Long = a*b

  val longResult = numbers.view.map(n => (n*n).toLong).filter(n => n%2 == 1).reduce(multLong)
  println(s"Long Result of multiplying odd squares from 1 to ${number*number} is $longResult")

  //so BigInt is a special numberic type for handling basically integers of unlimited size,
  // but the operations will not be as fast as with regular Int and Long that is the downside,
  // so use them only when you need them
  def multBig(a:BigInt, b:BigInt):BigInt = a*b

  //notice we need a different way of casting to BigInt
  val bigResult = numbers.view.map(n => BigInt(n*n)).filter(n => n%2 == 1).reduce(multBig)

  println(s"Big Integer Result of multiplying odd squares from 1 to ${number*number} is $bigResult")

  //now writing so called anonymous functions without type might actually be easier because the types will be picked up
  val biggieResult = numbers.view.map(n => BigInt(n*n)).filter(n => n%2 == 1).reduce(_*_) //we can let Scala figure out the correct type
  println(s"Alos a truly big result: $biggieResult")

  //so we can shorten this a tiny bit by using _ for filter as well
  val hugeResult = numbers.view.map(n => BigInt(n*n)).filter(_%2 == 1).reduce(_*_)
  println(s"Also a truly huge result: $hugeResult")

  //print product(multiplication) of odd squares of numbers from 1 to user input
  //so user enters 5
  //answer would be 1*9*25 = 225

  //so for reduce part you should define mult function or you could write it inline but that is a bit harder

  //goal would be to use view map filter reduce in a single line
  //you can do it first in separate operations of course


}
