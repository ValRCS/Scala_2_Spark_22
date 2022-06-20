package com.github.ValRCS

object Day4TraversalMethods extends App {
  //more Collections methods
  //https://alvinalexander.com/scala/how-to-choose-scala-collection-method-solve-problem-cookbook/

  val numbers = (-5 to 12).toArray
  val over5 = numbers.count(_ > 5)
  val alsoOver5 = numbers count { _ > 5} //so { has anonymous function inside
  println(s"There are $over5 == $alsoOver5 over 5 in ${numbers.toList}")

  //so we can use diff not only on sets!
  val n3_7 = (3 to 7).toArray
  val nDiff = numbers diff n3_7 //turns out it works even on Java Array, since Scala provides a nice wrapper around it
  println(nDiff.toList) //I am using toList instead of mkString just for quick printing

  val n5_9 = (5 to 9).toArray
  val n8_9 = n5_9.diff(n3_7)
  println(n8_9.toList)

  println(numbers.drop(8).mkString(","))

  //so like a reverse filter
  //https://stackoverflow.com/questions/51583361/scala-dropwhile-vs-filter
  //6
  //
  //dropWhile discards all the items at the start of a collection for which the condition is true.
  // It stops discarding as soon as the first item fails the condition.
  //so not QUITE the same as filter

  val clonedNumbers = numbers.concat(numbers)
  println(clonedNumbers.mkString(","))

  val positives = clonedNumbers.dropWhile(_ < 1) //so it stops dropping when the functions gives false
  println(positives.toList)
  val alsoPositive = clonedNumbers.filter(_ > 0) //filter will give us more results
  println(alsoPositive.toList)

  //real reverse Filter would be filterNot :)

  val negatives = clonedNumbers.filterNot(_ >= 0)
  val alsoNegatives = clonedNumbers.filter(_ < 0) //same as above

  println(negatives.toList)
  println(alsoNegatives.toList)


}
