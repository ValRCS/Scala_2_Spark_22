package com.github.ValRCS

object Day3MatchingParenthesis extends App {
  def areParenthesisMatching(text:String):Boolean =
    //TODO check if all opening ( are properly )
    // () -> true
    // (()()) -> true
    // (())() -> true
    // )()( -> false
    // ((()) -> false
    //TODO bonus if you can also ignore an non parenthesis
    //only thing here i think break or early return might help
    false //FIXME

  print(areParenthesisMatching("((()"))

}
