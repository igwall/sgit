package app.components
import app.components.{FileManager, Branch}

case class Head(sgitDirectory: String, content: String, branch: Branch) {
  def update(content: String, sgitDirectory: String) {
    FileManager.delete(s"$sgitDirectory/HEAD")
    FileManager.createFile("HEAD", content, sgitDirectory)
    //Update head of good branch:
    val newBranch = branch.update(content)
    newBranch.save()
  }

}

object Head {

  def apply(sgitDirectory: String): Head = {
    val content = FileManager.extractContentFromPath(s"$sgitDirectory/HEAD")
    val branchName =
      FileManager.extractContentFromPath(s"$sgitDirectory/BRANCH")
    val currentBranch = Branch.getBranch(sgitDirectory, branchName)
    new Head(sgitDirectory, content, currentBranch)
  }

}
