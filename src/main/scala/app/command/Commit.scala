package app.command
import app.components.Stage
import app.components.Head
import app.components.Tree
import app.components.FileManager

object Commit {

  def create(
      sgitDirectory: String,
      message: String,
      repoDirectory: String
  ): String = {
    //Prepare all the trees to save
    val stageContent = Stage.readStage(sgitDirectory)
    Stage.backup(sgitDirectory)
    val stageLines = stageContent.split("\n").toList
    val contentToSave = prepareContent(stageLines, repoDirectory, sgitDirectory)

    //Data about commit
    val name = "/"
    val olderCommit: String = Head.getLastCommit(sgitDirectory)
    val tree = Tree.createTree(name, contentToSave, sgitDirectory)
    val hash =
      FileManager.createHash(name + contentToSave.mkString + sgitDirectory)
    save(hash, olderCommit, tree.hash, message, sgitDirectory)
    hash
  }

  def prepareContent(
      listOfStageLines: List[String],
      repoDirectory: String,
      sgitDirectory: String
  ): List[List[String]] = {
    //On doit mettre le path.split + hash
    //val contentCleaned = deleteFilesCheck(listOfStageLines, repoDirectory, sgitDirectory)
    listOfStageLines
      .map(elem => transformPathLine(elem.split(s" ${Stage.separator} ")))
      .toList
  }

  def deleteFilesCheck(
      listOfStageLines: List[String],
      repoDirectory: String,
      sgitDirectory: String
  ): List[String] = {
    val deleteBlankLine = listOfStageLines.filter(line => line != "")
    val file = deleteBlankLine.map { line =>
      val split = line.split(s" ${Stage.separator} ")
      println(split(0) + "," + split(1))
      (split(0), split(1))
    }
    deleteTraitment(sgitDirectory, file)
  }

  def transformPathLine(line: Array[String]): List[String] = {
    val pathSplitted = line(1).split("/").toList
    val cleanedPathSplitted = pathSplitted.filter(path => path.size > 0)
    cleanedPathSplitted.dropRight(1) :+ line(0) //We replace the name of the file with is hash
  }

  def deleteTraitment(
      sgitDirectory: String,
      pathTuppled: List[(String, String)]
  ): List[String] = {
    val res = pathTuppled.partition(elem => FileManager.exist(elem._2))
    // Delete all the path from this list
    res._2.map(elem => Stage.delete(sgitDirectory, elem._2))
    // Call the stage to delete path
    res._1.map(tuple => tuple._2.toString())

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

  def extractContentLastCommit(sgitDirectory: String): String = {
    val commit = Head.getLastCommit(sgitDirectory)
    FileManager.extractContentFromPath(s"$sgitDirectory/commits/$commit")
  }

  def extractContentFromCommit(sgitDirectory: String, hash: String): String = {}

}
