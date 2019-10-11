package app.components
import app.components.FileManager

object Head {
  def update(content: String, sgitDirectory: String) {
    val headPath = sgitDirectory + "/HEAD"
    FileManager.delete(headPath)
    FileManager.createFile("HEAD", content, sgitDirectory)
  }

  def getLastCommit(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(s"${sgitDirectory}/HEAD")
  }
}
