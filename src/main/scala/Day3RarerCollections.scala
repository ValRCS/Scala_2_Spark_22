package com.github.ValRCS

import scala.util.Random

object Day3RarerCollections extends App {
  println("A look at some less common data structures in Scala collections")

  //so Array is actually the most basic data structure, it is just one long block of memory by indexes for that particular data type
  //usually Array will be your first choice for something that needs indexed access
  //for access by keyword we use Map
  //for uniques we use Set
  val seqSize = 100_000
  val maxNumber = 1_000_000

  val arr = (0 to seqSize).toArray
  val v1 = (0 to seqSize).toVector
  val list = (0 to seqSize).toList
  val seq = (0 to seqSize).toSeq //gives us range which is lazy gives good for looping but does not have the numbers yet

  val randomArray = arr.map(_ => Random.nextInt(maxNumber))
  val randomVector = v1.map(_ => Random.nextInt(maxNumber))
  val randomList = list.map(_ => Random.nextInt(maxNumber))
  val randomSeq = (0 to seqSize).map(_ => Random.nextInt(maxNumber))

  println(randomArray.take(10).mkString(","))
  println(randomVector.take(10))
  println(randomList.take(10))
  println(randomSeq.take(10))

  var t0 = System.nanoTime()
  val sortedArray = randomArray.sorted
  var t1 = System.nanoTime()

  Util.printDeltaMs(t0, t1, "Sorting Array time")

//  t0 = System.nanoTime()
//  val sortedVector = randomVector.sorted
//  t1 = System.nanoTime()
//  Util.printDeltaMs(t0, t1, "Sorting Vector time")
//
//  t0 = System.nanoTime()
//  val sortedList = randomList.sorted
//  t1 = System.nanoTime()
//  Util.printDeltaMs(t0, t1, "Sorting List time")
//
//  t0 = System.nanoTime()
//  val sortedSeq = randomSeq.sorted
//  t1 = System.nanoTime()
//  Util.printDeltaMs(t0, t1, "Sorting Seq time")

  val beg = 1_000
  val end = 300_000
  val increment = 10_000

  t0 = System.nanoTime()
  val slicedArray = randomArray.slice(beg, end).map(_+increment)
  t1 = System.nanoTime()
  Util.printDeltaMs(t0, t1, "Slicing and Mapping Array time")
//
//  t0 = System.nanoTime()
//  val slicedVector = randomVector.slice(beg, end).map(_+increment)
//  t1 = System.nanoTime()
//  Util.printDeltaMs(t0, t1, "Slicing and Mapping Vector time")
//
//  t0 = System.nanoTime()
//  val slicedList = randomList.slice(beg, end).map(_+increment)
//  t1 = System.nanoTime()
//  Util.printDeltaMs(t0, t1, "Slicing and Mapping List time")
//
//  t0 = System.nanoTime()
//  val slicedSeq= randomSeq.slice(beg, end).map(_+increment)
//  t1 = System.nanoTime()
//  Util.printDeltaMs(t0, t1, "Slicing and Mapping Sequence time")

  //so for basic operations all 4 structures will perform similarly, but Array and Vector will have an edge

  val queue = scala.collection.immutable.Queue.empty
  //if we have an immutable queue then the only way to proceed is to create new queue
  val addOne = queue.enqueue(3424)
  val addAnother = addOne.enqueue(43242)
  println(addAnother.dequeue) //so quuee functions as FIRST IN - FIRST OUT data structure

  //it will be more convenient to use a mutable queue
  val mutQueue = scala.collection.mutable.Queue[Int]() //so we will store integers in our queue
  mutQueue.enqueue(43,3423,62525)
  println(mutQueue)
  val firstOne = mutQueue.dequeue()
  println(firstOne)
  println(mutQueue.head)
  println(mutQueue.last) //this should be slower because we have to go through all the queue and find the last one
  println(mutQueue)
  //so .head just returns the first value, while dequeue() returns and removes the value for queue

  //so you would use queue whenever you actually need this first in first out functionality and not much else

  //so stack is actually one of the building blocks of computers it is a  data structure which is used for LIFO
  //Last In - First Out
  val stack = scala.collection.mutable.Stack[Int]()
  println(s"Stack size is ${stack.size}")
  stack.push(434) //so first entered value will be printed out last..
  stack.push(5246)
  stack.push(532521)
  println(stack) //notice it prints the last entered value first :)
  val popped = stack.pop()
  println(stack)
  println(stack.head)

  for (n <- randomArray) stack.push(n)

  println(stack.size)

  t0 = System.nanoTime()
  val sortedStack= stack.sorted //this should really be slow
  t1 = System.nanoTime()
  Util.printDeltaMs(t0, t1, "Sorted Stack time")



}
