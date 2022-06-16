package com.github.ValRCS

import java.io.{File, FileWriter}
import java.nio.file.{Files, Paths}
import scala.io.Source

object Util {
  //we are not going to use this directly thus not extends App
  //we will keep useful Utilities inside

  //let's make a our own round function
  //could come in handy
  def myRound(n: Double, precision: Int = 0): Double = {
    //so the trick would be to multiply by some power of 10 then divide by that mulitplier
    //so (n*100).round/100 would give us 2 points of precision
    //why because built in round gives us 0 precision
    //so if we have 100 that is 10 to the 2nd power(squared)
    //we can use the built in Math.pow to do that
    val multiplier = Math.pow(10, precision) //so precision 0 will give us 10 to 0 which is 1

    //    n.round //only 0 precision
    (n * multiplier).round / multiplier //we utilize the built in round
  }

  /**
   * finds delta from starting and stop time
   *
   * @param t0 - starting time
   * @param t1 - stop time
   * @return delta in milliseconds
   */
  def deltaMs(t0: Long, t1: Long, precision: Int = 3): Double = {
    myRound((t1 - t0) / 1_000_000.0, precision) //by default it will be 3
  }


  def printDeltaMs(t0: Long, t1: Long, taskName: String = "", precision: Int = 3): Unit = {
    val ms = deltaMs(t0, t1, precision = precision)
    println(s"It took $ms milliseconds to run the task: $taskName")
  }


  def getTextFromFile(src: String):String = {
    val bufferedSource = Source.fromFile(src) //think of bufferedSource as a stream of bytes
    val text = bufferedSource.mkString //we convert this stream into actual string
    bufferedSource.close() //important to close the file
    text
  }

  def getLinesFromFile(src: String):Array[String] = {
    val bufferedSource = Source.fromFile(src)
    val lines = bufferedSource.getLines().toArray
    bufferedSource.close()
    lines
  }

  /**
   *
   * @param dstPath - save Path
   * @param text - string to save
   */
  def saveText(dstPath: String,
               text: String,
               append:Boolean=false,
               verbose:Boolean=false,
               prepend:String="\n"):Unit = {
    //    import java.io.{PrintWriter, File} //explicit import
    if (verbose) println(s"Saving ${text.length} characters to $dstPath")
    //so writing to file can be done either by overwriting the whole file (the default)
    //or by appending to the end of the file
    val fw = new FileWriter(dstPath, append) //so by default old dstPath will be overWritten
    //    val pw = new PrintWriter(new File(dstPath))
    if (append) fw.write(prepend) //by default we are appending "\n"
    fw.write(text)
    fw.close() //when writing it is especially important to close as early as possible
  }

  /**
   *
   * @param dstPath - save Path
   * @param lines - array of Strings to save
   *              overwrites old file by default
   */
  def saveLines(dstPath: String, lines: Array[String], append:Boolean=false, lineEnd:String="\n"):Unit = {
    saveText(dstPath, lines.mkString(lineEnd), append)
  }

  /**
   *
   * @param url - web resource locator
   * @return - we return the whole web page as string
   */
  def getTextFromWeb(url: String): String = {
    val html = Source.fromURL(url) //this gets us BuffereSource stream
    html.mkString //so we just get a string representation it could be pure txt, it be html, it be xml,
  }

  /**
   *
   * @param url - web resource locator
   * @param dst - destination file path
   * @return - returns text string from the url (could be txt, html, xml, json, etc)
   */
  def getTextFromWebAndSave(url: String, dst: String):String = {
    val text = getTextFromWeb(url)
    saveText(dst, text)
    text //we return the text just in case we want to save and do some work as well
  }

  /**
   * get a list of Files
   * @see adopted from [[https://alvinalexander.com/scala/how-to-list-files-in-directory-filter-names-scala/]]
   * @param dir - listing path
   * @param regex - match to filter by, default is all files of name length 1 or more
   * @return - returns list of Files
   */
  def getListOfFiles(dir: String, regex:String=".*"):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      //      d.listFiles.filter(_.isFile).toList
      d.listFiles.filter(file => file.isFile && file.getName.matches(regex) ).toList //FIXME regex
    } else {
      List[File]() //we return an empty list of Files if nothing is found
    }
  }

  /**
   * returns true if file exists at the location, false otherwise
   * @param filePath - represents OS file path
   * @return
   */
  def isFileTHere(filePath:String):Boolean = {
    val path = Paths.get(filePath)
    Files.exists(path)
  }

  /**
   *
   * @param lines - array of strings
   * @param newLine - how to join the lines
   * @return
   */
  def getCharacterCount(lines: Array[String], newLine:String="\n"):Int = {
    lines.mkString(newLine).length //we build up a string and return its length
  }

  //return wordCount for each Line
  def getWordCountPerLine(lines: Array[String], sep:String=" +"):Array[Int] = {
    val wordsLines = lines.map(_.split(sep)) //so we get an Array of Array of words
    val wordsPerLine = wordsLines.map(_.length)
    wordsPerLine
  }
}
