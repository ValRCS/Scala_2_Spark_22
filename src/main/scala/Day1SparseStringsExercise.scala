package com.github.ValRCS

object Day1SparseStringsExercise extends App {
  //https://www.hackerrank.com/challenges/sparse-arrays/problem
  //There is a collection of input strings and a collection of query strings.
  // For each query string, determine how many times it occurs in the list of input strings.
  // Return an array of integers as results

  val strings = Array("Valdis","alus","aldaris","Aiviekste","Bauska","Valdis")
  val queries = Array("Bauska","Valdis","Cēsis")

//  val result = Array(1,2,0)

  def getStringMatches(strings:Array[String], queries:Array[String]):Array[Int] = {

    Array(1,2,0) //correct for the above but FIXME :)
  }

  val results = getStringMatches(strings, queries)

  println(results.mkString(","))



}
