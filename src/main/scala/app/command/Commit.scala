package app.command
import app.components.Stage
import app.components.Head
import app.components.Tree
import app.components.FileManager

object Commit {

  def create(sgitDirectory: String, message: String): String = {
    //Prepare all the trees to save
    val stageLines = Stage.readStage(sgitDirectory).split("\n").toList
    val contentToSave = prepareContent(stageLines)

    //Data about commit
    val name = "/"
    val olderCommit: String = Head.getLastCommit(sgitDirectory)
    val tree = Tree.createTree(name, contentToSave, sgitDirectory)
    val hash =
      FileManager.createHash(name + contentToSave.mkString + sgitDirectory)
    save(hash, olderCommit, tree.hash, message, sgitDirectory)
    hash
  }

  def prepareContent(listOfStageLines: List[String]): List[List[String]] = {
    //On doit mettre le path.split + hash
    val separedBlobAndPath =
      listOfStageLines
        .map(elem => transformPathLine(elem.split(" :: ")))
        .toList
    separedBlobAndPath
  }

  def transformPathLine(line: Array[String]): List[String] = {
    val pathSplitted = line(1).split("/").toList
    val cleanedPathSplitted = pathSplitted.filter(path => path.size > 0)
    cleanedPathSplitted.dropRight(1) :+ line(0) //We replace the name of the file with is hash
  }

  def save(
      hash: String,
      olderCommit: String,
      tree: String,
      message: String,
      sgitDirectory: String
  ) {
    //Save new commit hash on HEAD
    Head.update(hash, sgitDirectory)

    //Save informations about commit in /commits
    val content =
      s"oldCommit : $olderCommit\ntrees: $tree\nmessage: $message"
    val path = sgitDirectory + "/commits"
    FileManager.createFile(hash, content, path)
  }

}
