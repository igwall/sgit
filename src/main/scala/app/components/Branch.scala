package app.components
import app.components.{FileManager, Head}

object Branch {

  def createBranch(name: String, sgitDirectory: String): Option[String] = {
    val lastCommit = Head.getLastCommit(sgitDirectory)
    // If we don't have any commit existing yet
    if (lastCommit.isEmpty()) {
      return None
    } else {
      val error =
        FileManager.createFile(name, lastCommit, getPath(sgitDirectory))
      if (error.isDefined) error else Some("Branch correctly created")
    }
  }

  def getPath(sgitDirectory: String): String = {
    sgitDirectory + "/branches/"
  }
}
