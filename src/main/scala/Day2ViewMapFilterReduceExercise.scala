package com.github.ValRCS

import scala.io.StdIn.readLine

object Day2ViewMapFilterReduceExercise extends App {
  println("Ask user for input")
  val number = readLine("Please Enter a number").toInt //you could add error checking if you remember from last class
  println(number)

  //print product(multiplication) of odd squares of numbers from 1 to user input
  //so user enters 5
  //answer would be 1*9*25 = 225

  //so for reduce part you should define mult function or you could write it inline but that is a bit harder

  //goal would be to use view map filter reduce in a single line
  //you can do it first in separate operations of course
}
