package app.components
import app.components.FileManager
import java.io.File

object Head {
  def update(content: String, sgitDirectory: String) {
    val headPath = s"${sgitDirectory}${File.separator}.old${File.separator}HEAD"
    FileManager.delete(headPath)
    FileManager.createFile("HEAD", content, sgitDirectory)
  }

  def getLastCommit(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(sgitDirectory + File.separator + "HEAD")
  }
}
