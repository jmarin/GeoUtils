package com.marinotero.util
import java.io.File

import java.io.PrintWriter

import java.io.FileWriter
import java.io.Serializable

class FileUtil extends Utils {

  def fileList(path: File, extension: String): Array[File] = {
    val files = path.listFiles().filter(f => f.getName().endsWith("." + extension))
    files
  }

  def writeToFile(fileName: String, data: String) = using(new FileWriter(fileName)) { fileWriter =>
    fileWriter.write(data)
  }

  def appendToFile(fileName: String, textData: String) =
    using(new FileWriter(fileName, true)) { fileWriter =>
      using(new PrintWriter(fileWriter)) { printWriter =>
        printWriter.println(textData)
      }
    }
}