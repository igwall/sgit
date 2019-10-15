package app.components
import app.components.{FileManager, Branch}

object Head {
  def update(content: String, sgitDirectory: String) {
    val headPath = sgitDirectory + "/HEAD"
    FileManager.delete(headPath)
    FileManager.createFile("HEAD", content, sgitDirectory)

    //Update head of good branch:
    Branch.update(sgitDirectory, getLastCommit(sgitDirectory))
  }

  def getLastCommit(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(s"${sgitDirectory}/HEAD")
  }
}
