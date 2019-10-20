package app.components
import app.components.{FileManager, Head}
import java.io.File

case class Tag(
    sgitDirectory: String,
    branchName: String,
    lastCommit: String
) {
  val tagPath = s"$sgitDirectory/tags/$branchName"

  def save() {
    FileManager.delete(tagPath)
    FileManager.createFile(
      branchName,
      lastCommit,
      s"$sgitDirectory/tags/"
    )
  }
  // Return a new branch that will be saved
  def update(commitHash: String): Branch = {
    new Branch(sgitDirectory, branchName, commitHash)
  }
}
object Tag {

  def getTag(sgitDirectory: String, branchName: String): Branch = {
    new Branch(
      sgitDirectory,
      branchName,
      FileManager.extractContentFromPath(s"$sgitDirectory/tags/$branchName")
    )
  }

  def createTag(
      name: String,
      sgitDirectory: String,
      head: Head
  ): Option[String] = {
    // If we don't have any commit existing yet
    if (head.content.isEmpty()) {
      return None
    } else {
      val message =
        FileManager.createFile(name, head.content, s"$sgitDirectory/tags")
      if (message.isDefined) Some("Tag correctly created")
      else Some("Tag already existing")
    }
  }

  def getAllTag(sgitDirectory: String): String = {
    val allFiles = new File(s"$sgitDirectory/tags/").listFiles().toList
    allFiles
      .map(
        file => file.getCanonicalFile().toString().split("/").toList.last + "\n"
      )
      .mkString
  }
}
