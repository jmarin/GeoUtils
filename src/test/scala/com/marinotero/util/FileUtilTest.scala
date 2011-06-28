package com.marinotero.util
import scala.io.Source

import org.junit._
import Assert._

class FileUtilTest {

  val pathSeparator = System.getProperty("file.separator")
  val fileUtil = new FileUtil

  @Test
  def writeToFileTest() {
    val filePath = System.getProperty("user.dir") + pathSeparator + "target" + pathSeparator + "fileTest.txt"
    val data = "Hello World"
    fileUtil.writeToFile(filePath, data)
    val file = Source.fromFile(filePath)
    val lines = file.getLines().toArray
    assertEquals(lines(0),"Hello World")
  }
  
  @Test
  def appentToFileTest(){
    val filePath = System.getProperty("user.dir") + pathSeparator + "target" + pathSeparator + "fileTest.txt"
    val file = Source.fromFile(filePath)
    val lines = file.getLines().toArray
    assertEquals(lines(0),"Hello World")
    val data = "\nNew Line"
    fileUtil.appendToFile(filePath,data)
    val file2 = Source.fromFile(filePath)
    val lines2 = file2.getLines().toArray
    assertEquals(lines2(1),"New Line")
    
  }

}