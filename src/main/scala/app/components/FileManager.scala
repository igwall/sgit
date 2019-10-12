package app.components
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import scala.io.Source
import java.security.MessageDigest

object FileManager {
  def searchFolder(file: File, folder: String): Option[File] = {
    val newFileToFind = new File(file.toString() + File.separator + folder)
    if (newFileToFind.exists()) {
      Some(newFileToFind)
    } else {
      if (file.getParent() == null) {
        return None
      } else {
        searchFolder(file.getParentFile(), folder)
      }
    }
  }

  def extractContentFromPath(path: String) = {
    scala.io.Source.fromFile(path).mkString
  }

  def createFile(name: String, data: String, path: String): Unit = {
    val file = new File(path + File.separator + name)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(data)
    bw.close()
  }

  def delete(path: String): Unit = {
    val file = new File(path)
    if (file.isDirectory()) {
      file.listFiles().foreach(f => delete(f.getCanonicalPath()))
    }
    file.delete()
  }

  def createHash(content: String): String = {
    MessageDigest
      .getInstance("SHA-1")
      .digest(content.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString

  }

  def exist(path: String): Boolean = {
    return new File(path).exists
  }
}
