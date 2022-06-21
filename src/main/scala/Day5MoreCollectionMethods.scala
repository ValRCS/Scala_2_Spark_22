package com.github.ValRCS

object Day5MoreCollectionMethods extends App {
  val numbers = (-5 to 7).toList

  println(numbers.hasDefiniteSize) //this is depreceated , we used this because some collections
  //like something coming as a stream from a network, might not have a known size
  //now we would use knownSize
  println(numbers.knownSize) //recommended now , returns -1 because List needs traversal for size calc
  println(numbers.toArray.knownSize)
  println(numbers.length) //this actually requires going /traversing the whole numbers List

  val emptyArr = Array[Int]() //empty array
  println(emptyArr.headOption.getOrElse(-9000)) //so returns Option , useful if you might get empty collection
  println(numbers.headOption.getOrElse(-9000))

  val allButLast = numbers.init //so gets everything but the last element
  println(allButLast)
  val n3_11 = (3 to 11).toArray

  val number_intersection = numbers.intersect(n3_11) //so we can interset List with Array
  val also_intersection = n3_11.intersect(numbers) // here we get Array but same insides
  println(number_intersection)
  println(also_intersection.mkString(","))
  //so we can use the usual set operations on other collections
  //TODO it should work also on maps as well

  //so similar to headOption , we can also get lastOption - that is last item Some(item) or None
  println(numbers.lastOption.getOrElse(555))
  println(emptyArr.lastOption.getOrElse(-9000))

//  val doubledNumbers = numbers.concat(numbers)
  val doubledNumbers = numbers ++ numbers //so same as above
  println(doubledNumbers.span(_ < 3)) //this stops at first false
  println(doubledNumbers.partition(_ <  3)) //this partitions fully

//  val numberUnion = doubledNumbers.union(n3_11) - depreceated use the one below
  val numberUnion = doubledNumbers ++ n3_11
  println(numberUnion)
  //for uniques of course we would convert to set
  println(numberUnion.toSet)
  println(numberUnion.toSet.toSeq.sorted) //so our uniques sorted

  val listTuples = List((3,"valdis"), (10, "Līga"), (20, "Jānis"))
  val (col1, col2) = listTuples.unzip //unzips collection into two
  println(col1)
  println(col2)

  val letters = "abcdefgh"
  val zippedCol = numbers zip letters //so when zipping it will stop zipping when one collection runs out
  println(zippedCol)

  //when you have some collection and you want to provide numbering
  val indexedCol = letters.zipWithIndex
  println(indexedCol) //so actual value is first the index is 2nd
  println(indexedCol(3)) //prints tuple (d,3)


}
