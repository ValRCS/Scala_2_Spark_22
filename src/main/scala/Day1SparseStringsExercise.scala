package com.github.ValRCS

object Day1SparseStringsExercise extends App {
  //https://www.hackerrank.com/challenges/sparse-arrays/problem
  //There is a collection of input strings and a collection of query strings.
  // For each query string, determine how many times it occurs in the list of input strings.
  // Return an array of integers as results

  val strings = Array("Valdis","alus","aldaris","Aiviekste","Bauska","Valdis","Valdis")
  val queries = Array("Bauska","Valdis","Cēsis")


  println(strings.count(el => el == "Valdis"))
  println(strings.count(_ == "Valdis"))  // same as above but shorter

  val numbers = (0 to 12).toArray
  val evenCount = numbers.count(_ % 2 == 0)
//  val evenCount = numbers.count(el => el % 2 == 0) //same as above
  //notice how in { no need to escape " " }
  println(s"There are $evenCount even numbers in ${numbers.mkString(",")} array")

//  val result = Array(1,2,0)

  def getStringMatches(strings:Array[String], queries:Array[String]):Array[Int] = {
//    val arrayOfMatches = queries.map(qry => strings.count(entry => entry == qry))
//    arrayOfMatches.foreach(println)
//    arrayOfMatches
//    Array(1,2,0) //correct for the above but FIXME :)
//    queries.map(qry => strings.count(entry => entry == qry)) //this syntax lets you do more stuff with entry
    queries.map(qry => strings.count(_ == qry)) //same as above in this case
  }

  val results = getStringMatches(strings, queries)

  println(results.mkString(","))

  //what happens if you have a lot of strings and a lot of queries?
  //then you map and count will go through each query and perform count
  //so that is in effect a double nested loop in a regular language
  //so map hides one loop (outer loop) and count hides another inner loop

  //so if you have more than a few queries or more than a few strings then you need a more optimal approach
  //it might not be as clean but it will be faster

  //big idea is to use Map type of data structure and simply go through strings only one time
  //then we could use queries for checking keys to create a new Map

  //so grouped will slice a sequence in specific subsequences of certain size
  val grouped = strings.grouped(3).toArray // not what we want this just divides
  grouped.foreach(el => println(el.mkString(",")))

  //we could group by elements by their hashcode since each string has a unique representation
  val groupedBy = strings.groupBy(_.hashCode)
  groupedBy.foreach(el => println(el))

  /**
   * TOO long a function name, unless really needed stick to max 3 or 4 words, ideally 2
   * @param strings
   * @param queries
   * @return
   */
  def getStringMatchesOptimizedForLargeQueries(strings:Array[String], queries:Array[String]):Array[Int] = {
    //initialize Mutable Map
    val mutableMap = collection.mutable.Map[String,Int]()

    //key idea is that we can acess and modify values by key very very quickly even in a large map
    //if key exists we increase the value by 1
    //if key does not exist we set the value to 1
    //advantage of this approach we only need to go through strings once
    //not multiple times with each count in the above exampl
    for (text <- strings) {
      if (mutableMap.contains(text)) mutableMap(text) += 1
      else mutableMap(text) = 1
    }
//    for ((text, cnt) <- mutableMap ) {
//      println(s"$text occurs $cnt times")
//    }
    //so using getOrElse we can provide a default value
    //  println(mutableMap.getOrElse("Valdis", 0))
    //  println(mutableMap.getOrElse("Līga", 0))
  val solution = queries.map(qry => mutableMap.getOrElse(qry, 0))
    solution
  }




//

  val solution = getStringMatchesOptimizedForLargeQueries(strings, queries)
  println(solution.mkString(","))

  val alpha = 'a' to 'z'
  //funny but here hashCode actually corresponds to characters ASCII code
  //not very secure hash function
  for (letter <- alpha) {
    println(s"$letter hashes to ${letter.hashCode()}")
  }

  println("abba".hashCode)
  println("abbb".hashCode)

  println("hopefully a big big big document ".hashCode)


}
