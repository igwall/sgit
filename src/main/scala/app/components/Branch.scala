package app.components
import app.components.{FileManager, Head}

object Branch {

  def createBranch(name: String, sgitDirectory: String): Option[String] = {
    val lastCommit = Head.getLastCommit(sgitDirectory)
    // If we don't have any commit existing yet
    if (lastCommit.isEmpty()) {
      return None
    } else {
      val message =
        FileManager.createFile(name, lastCommit, getPath(sgitDirectory))
      if (message.isDefined) Some("Branch correctly created")
      else Some("Branch already existing")
    }
  }

  def getPath(sgitDirectory: String): String = {
    sgitDirectory + "/branches/"
  }

  def getCurrentBranch(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(s"$sgitDirectory/BRANCH")
  }

  def setCurrentBranch(sgitDirectory: String, branchName: String) = {
    val branchPath = sgitDirectory + "/branches/" + getCurrentBranch(
      sgitDirectory
    )
    FileManager.delete(branchPath)
    FileManager.createFile("BRANCH", branchName, sgitDirectory)
  }

  def update(sgitDirectory: String, content: String) = {
    val branchPath = sgitDirectory + "/branches/" + getCurrentBranch(
      sgitDirectory
    )
    FileManager.delete(branchPath)
    FileManager.createFile(
      getCurrentBranch(sgitDirectory),
      content,
      s"$sgitDirectory/branches/"
    )
  }

  def getLastCommit(sgitDirectory: String): String = {
    val currentBranch = getCurrentBranch(sgitDirectory)
    FileManager.extractContentFromPath(
      s"$sgitDirectory/branches/$currentBranch"
    )
  }
  def getCommitFromBranch(sgitDirectory: String, branchName: String): String = {
    FileManager.extractContentFromPath(
      s"$sgitDirectory/branches/$branchName"
    )
  }
}
