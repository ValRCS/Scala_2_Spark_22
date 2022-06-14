package com.github.ValRCS

object Day1Main {
  def main(args: Array[String]): Unit = {
    println("Look Ma I can write my own main function!")
    println("No need for extends App")
    //args are commonly used to pass along values between different command line applications
    //also to give some values ar parameters
    for (arg <- args) {
      println(s"Received $arg as argument")
    }
  }
}
