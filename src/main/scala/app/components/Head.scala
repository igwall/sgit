package app.components
import app.components.FileManager
import java.io.File

object Head {
  def update(content: String, sgitDirectory: String) {
    FileManager.delete(s"$sgitDirectory/HEAD")
    FileManager.createFile("HEAD", content, sgitDirectory)
    //Update head of good branch:
    Branch.update(sgitDirectory, getLastCommit(sgitDirectory))
  }

  def getLastCommit(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(sgitDirectory + File.separator + "HEAD")
  }
}
