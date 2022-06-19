package com.github.ValRCS

object Day3MatchingParenthesis extends App {
  def areParenthesisMatching(text:String):Boolean = {
    //TODO check if all opening ( are properly )
    // () -> true
    // (()()) -> true
    // (())() -> true
    // )()( -> false
    // ((()) -> false
    //TODO bonus if you can also ignore an non parenthesis
    //only thing here i think break or early return might help
    var lCount = 0
    for (ch <- text) {
      if (ch == '(' ) lCount+=1 //so this is always allowed
      if (ch == ')' ) {
        if (lCount > 0) lCount-=1 //we close one bracket
        else return false //we have no matching matching brackets for ) to close on
        //this is one of those cases where early return makes sense to me the most
        //if we did not want early return we could have var isGood and use that
      }
    }
    lCount == 0 //so our success condition is true if there are no brackets left

  }

  def areParenthesisMatchingStack(text:String):Boolean = {
    //TODO check if all opening ( are properly )
    // () -> true
    // (()()) -> true
    // (())() -> true
    // )()( -> false
    // ((()) -> false
    //TODO bonus if you can also ignore an non parenthesis
    //only thing here i think break or early return might help

    val parentheses = text.filter(char => char=='(' | char==')')

    val parenthesisStack = scala.collection.mutable.Stack[Char]()
    for (char <- parentheses) {
      if (char == '(')
      {
        parenthesisStack.push(char)
      }
      else try {
        parenthesisStack.pop()
      }
      catch {
        case e: NoSuchElementException => return false
      }
    }

    if (parenthesisStack.isEmpty) true else false

  }

  println(areParenthesisMatching("((()()))dfafdafd"))
  println(areParenthesisMatching("((()"))
  println(areParenthesisMatching(")()(")) // ) too early
  println(areParenthesisMatching("Valdis (Val) went walking (running) ((hmmm)())"))
  println(areParenthesisMatching("Valdis (Val) went walking (running) ((hmmm)()")) //not enough )
  println(areParenthesisMatching("Valdis (Val) went walking (running) ((hmmm)()))")) // too many )
  println(areParenthesisMatching(")))((("))
  println(areParenthesisMatching("())))(((()")) //should be false

  println("\nNow the Stack version\n")

  println(areParenthesisMatchingStack("((()()))dfafdafd"))
  println(areParenthesisMatchingStack("((()"))
  println(areParenthesisMatchingStack(")()(")) // ) too early
  println(areParenthesisMatchingStack("Valdis (Val) went walking (running) ((hmmm)())"))
  println(areParenthesisMatchingStack("Valdis (Val) went walking (running) ((hmmm)()")) //not enough )
  println(areParenthesisMatchingStack("Valdis (Val) went walking (running) ((hmmm)()))")) // too many )
  println(areParenthesisMatchingStack(")))((("))
  println(areParenthesisMatchingStack("())))(((")) //should be false

  //so Harder exercise would be to match properly ( { [ with ] } )

  //so ([{]}) -> false because improper nesting
  // of course }])([{ also falsoe ->

  //how to solve this?
  //we will keep stack of opening parenthesis
  //when closing we will check for proper match
  val parenthesis = Map(('(' -> ')'),('[' -> ']'),('{' -> '}'))
//  val openParenthesis = parenthesis.keys.toSet
//  val closingParenthesis = parenthesis.values.toSet
  println(parenthesis)
//  print(openParenthesis)

  //now we can use this function to check on whatever mapping we give it
  def isCodeWellFormed(text:String,
                       parenthesis:Map[Char,Char]=parenthesis
                      ):Boolean = {
    val stack = scala.collection.mutable.Stack[Char]()
    val openParenthesis = parenthesis.keys.toSet
    val closingParenthesis = parenthesis.values.toSet

    for (c <- text) {
      if (openParenthesis.contains(c)) {
        stack.push(c)
      } else if (closingParenthesis.contains(c)) {
        //turns out we cant get stack.head if stack is empty
        if (stack.nonEmpty && parenthesis(stack.head) == c) stack.pop() //we got a match we are good to continue
        else return false
      }
    }

    stack.isEmpty
  }

  println(isCodeWellFormed("((()()))dfafdafd"))
  println(isCodeWellFormed("((()"))
  println(isCodeWellFormed(")()(")) // ) too early
  println(isCodeWellFormed("Valdis (Val) went walking (running) ((hmmm)())"))
  println(isCodeWellFormed("Valdis (Val) went walking (running) ((hmmm)()")) //not enough )
  println(isCodeWellFormed("Valdis (Val) went walking (running) ((hmmm)()))")) // too many )
  println(isCodeWellFormed(")))((("))
  println(isCodeWellFormed("())))((("))

  println(isCodeWellFormed("([{]})")) //should be false because of bad nesting
  println(isCodeWellFormed("Valdis (Val) likes { something } even indexes [{},{}, (),()]"))
  println(isCodeWellFormed("")) //should be true

}
