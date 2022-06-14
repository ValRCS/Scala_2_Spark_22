package com.github.ValRCS

object Day2Views {
  def main(args:Array[String]): Unit = {
    println("Creating temporary Views")

    val numbers = (0 to 12).toArray
    println(numbers.mkString(","))
    //our bread and butter is map, filter plus other operations on our collections
    val squares = numbers.map(n => n*n)
    println(squares.mkString(","))
    val evenSquares = squares.filter(n => n%2 == 0)
    println(evenSquares.mkString(","))
    //often we want to chain these operations in a single line
    val firstEvenSquares = numbers.map(n => n*n).filter(n => n%2 == 0).take(3) //first 3 even squares
    println(firstEvenSquares.mkString(","))

    //the slight problem with the above single line is that each map and filter creates  a new data collection in memory
    //this can be quite wasteful and slow operations down on larger data sets
    //again not a problem with tiny data sets such as this one (pretty much anything under a million items)

  }
}
