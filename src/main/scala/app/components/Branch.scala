package app.components
import app.components.{FileManager, Head}
import java.io.File

case class Branch(
    sgitDirectory: String,
    branchName: String,
    lastCommit: String
) {
  val branchPath = s"$sgitDirectory/branches/$branchName"

  def save() {
    FileManager.delete(branchPath)
    FileManager.createFile(
      branchName,
      lastCommit,
      s"$sgitDirectory/branches/"
    )
  }
  // Return a new branch that will be saved
  def update(commitHash: String): Branch = {
    new Branch(sgitDirectory, branchName, commitHash)
  }
}
object Branch {

  def getBranch(sgitDirectory: String, branchName: String): Branch = {
    new Branch(
      sgitDirectory,
      branchName,
      FileManager.extractContentFromPath(s"$sgitDirectory/branches/$branchName")
    )
  }

  def createBranch(
      name: String,
      sgitDirectory: String,
      head: Head
  ): Option[String] = {
    // If we don't have any commit existing yet
    if (head.content.isEmpty()) {
      return None
    } else {
      val message =
        FileManager.createFile(name, head.content, s"$sgitDirectory/branches")
      if (message.isDefined) Some("Branch correctly created")
      else Some("Branch already existing")
    }
  }

  def setCurrentBranch(sgitDirectory: String, branchName: String) = {
    FileManager.delete(s"$sgitDirectory/BRANCH")
    FileManager.createFile("BRANCH", branchName, sgitDirectory)
  }

  def getAllBranches(sgitDirectory: String): String = {
    val allFiles = new File(s"$sgitDirectory/branches/").listFiles().toList
    allFiles
      .map(
        file => file.getCanonicalFile().toString().split("/").toList.last + "\n"
      )
      .mkString
  }
}
