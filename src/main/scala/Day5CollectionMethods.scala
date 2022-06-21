package com.github.ValRCS

object Day5CollectionMethods extends App {
  println("More collection methods")
  //https://alvinalexander.com/scala/how-to-choose-scala-collection-method-solve-problem-cookbook/

  val numbers = (-5 to 7).toArray
  println(numbers.mkString(","))
  val duplicateNumbers = numbers.concat(numbers)
  println(duplicateNumbers.mkString(","))

  val droppedNeg = duplicateNumbers.dropWhile(_ < 0) //again dropWhile drops until predicate function stops being true
  println(droppedNeg.mkString(","))
  val takeFour = duplicateNumbers.take(4) //take first five
  println(takeFour.toList)

  val takeWhileNumbers = duplicateNumbers.takeWhile(_ < 0) //take items until predicates stops being true
  println(takeWhileNumbers.toList)

  //there is even a span method which will give us a collection of both dropWhile and takeWhile
  val spanCollection = duplicateNumbers.span(_ < 2) //so we get a tuple of two arrays
  println(spanCollection._1.mkString(",")) //so this will takeWhile
  println(spanCollection._2.mkString(",")) //and dropWhile

  //if we did not know span we could do the same as above by hand
//  val alsoSpanCollection = (duplicateNumbers takeWhile {_ < 2}, duplicateNumbers dropWhile {_ < 2})
  //same as above line
  val alsoSpanCollection = (duplicateNumbers.takeWhile(_ < 2), duplicateNumbers.dropWhile(_ < 2))
  println(alsoSpanCollection._1.mkString(",")) //so this will takeWhile
  println(alsoSpanCollection._2.mkString(",")) //and dropWhile

  val firstEvenNumber = duplicateNumbers.find(_ % 2 == 0) //find first item on which predicate function returns true
  println(firstEvenNumber) //returns Some because we might not find anything right ?
  println(firstEvenNumber.getOrElse(0)) //here using 0 to indicate we found nothing, adjust as needed

  val arrTuples = Array(List(3,5),List(35,32,2,6,1),List(3,1,6,6)) //using List because I am lazy to print
  println(arrTuples.mkString(","))

  val flatArr = arrTuples.flatten
  println(flatArr.mkString(","))

//  val arr3D = Array(Array(Array(3,1,5),Array(3,2,1,6)), Array(Array(3,1,5,1),Array(3,1,5,99)))
  val list3D = List(List(List(3,1,5),List(3,2,1,6)), List(List(3,1,5,1),List(3,1,5,99)))
  println(list3D(1))
  println(list3D(1)(1))
  println(list3D(1)(1)(3))
  println(list3D.last.last.last)

  val flatList = list3D.flatten
  println(flatList)
  val moreFlatList = flatList.flatten
  println(moreFlatList)
  val flatterList = list3D.flatten.flatten //so 3D sequence needs to be flattened twice
  println(flatterList)

  //there is flatMap which combines flatten with map
//  val flattenedAndMapped = arrTuples.flatMap(n => n) //not quite what we want
  println(arrTuples.toList)
  val flattenedAndMapped = arrTuples.flatMap(n => n.map(_*100))
  println(flattenedAndMapped.toList)
  val dupeInternalItems = arrTuples.flatMap(list => list.concat(list))
  println(dupeInternalItems.toList)

  val fruits = Seq("apple", "banana", "orange")
  println(fruits.map(_.toUpperCase))
  println(fruits.map(_.toUpperCase).flatten)
  println(fruits.flatMap(_.toUpperCase))

  val allLessThan100 = duplicateNumbers.forall(_ < 100) //returns True if all of them are less than 100
  println(allLessThan100)
  println(duplicateNumbers.forall(_ < 2)) //false because some are 2 or more

  //we can use foreach instead of for loop but foreach mostly would be used for side effects such as println
  //we provide a function to do something with each item, could be print, network call, save into database some side effect
  //if you want to save into a different data then map or for yield construction would be preferable
  duplicateNumbers.foreach(n => println(s"Number is $n"))

  //so groupBy takes a collection and groups it according to function we provide - analogies with Group By in SQL

  val evenOddMap = numbers.groupBy(_ % 2)
  for ((key, value) <- evenOddMap) {
    println(s"Reminder $key - > ${value.toList}")
  }
  //so % works slightly differently on negative numbers using % on negative odd number will give us -1 not 1
  //depends on language - https://stackoverflow.com/questions/13683563/whats-the-difference-between-mod-and-remainder

  //again Seq defaults to List for smaller sequences and to Vector for longer ones
  val moreFruits = fruits ++ Seq("peaches", "nectarines", "cherries", "pears")
  println(moreFruits)
  val groupedByLen = moreFruits.groupBy(_.length)
  for ((key, value) <- groupedByLen) {
    println(s"Length $key - > ${value.toList}")
  }

}
