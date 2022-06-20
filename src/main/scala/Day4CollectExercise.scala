package com.github.ValRCS

object Day4CollectExercise extends App {
  println("Day 4 Exercise on Partial Functions and collect")
  //TODO write two partial functions
  //getEvenSquare applies to only positive even numbers  -> returns square
  //getOddCube applies to only positive odd numbers -> returns cube
  //combine both partial functions into a single partial function
  //doPositives will work on positive numbers
  val numbers = (-5 to 28).toArray
  //using collect get the new values into
  //TODO
  //val processedNumbers =
  //println the results

  val getEvenSquares = new PartialFunction[Int, Int] {
    def apply(n: Int): Int = n*n
    def isDefinedAt(n: Int) = n > 0 && n%2 == 0
  }

  //alternative would be to use case syntax
  //notice we do not have match
  val getOddCubes: PartialFunction[Int, Int]  = {
    case x: Int
      if ((x > 0) && (x % 2 == 1)) => x*x*x
      //we could add more cases underneath
  }

  //what happens if we add another PartialFunction which overlaps previous ones
  val getSmallNumbers: PartialFunction[Int, Int] = {
    case n: Int
      if -3 < n && n < 3 => n * 1000 //so this overlaps with previous PartialFunctions
  }

  val evenSquareOddCubes = getEvenSquares orElse getOddCubes //i could add more partial functions here
  val getEverything = getEvenSquares orElse getOddCubes orElse getSmallNumbers //i could add more partial functions here

  val collectedNumbers = numbers.collect(evenSquareOddCubes)

  println(collectedNumbers.mkString(","))

  val numbersCollected = numbers collect getEverything // so we could use syntax without . same as previous collect call

  println(numbersCollected.mkString(","))

  //So if we combine multiple Partial functions
  // the winner in collect call will be the first one with defined argument for that particular value
  // we saw that getSmallNumbers worked only where getEvenSquares and getOddCubes did not

  println(getEverything.isDefinedAt(-3))
  println(getEverything.isDefinedAt(-2))

}
