package app.command
import app.components.Stage
import app.components.Head
import app.components.Tree
import app.command.Status
import app.components.{FileManager,Log}
import java.io.File


object Commit {

  def create(
      sgitDirectory: String,
      message: String,
      repoDirectory: String
  ): Option[String] = {
    //Prepare all the trees to save
    val stageContent = Stage.readStage(sgitDirectory)
    Stage.backup(sgitDirectory)
    val stageLines = stageContent.split("\n").toList
    val contentToSave = prepareContent(stageLines, repoDirectory, sgitDirectory)
    if (contentToSave.isDefined) {
      val name = "/"
      val olderCommit: String = Head.getLastCommit(sgitDirectory)
      val tree = Tree.createTree(name, contentToSave.get, sgitDirectory)
      val hash =
        FileManager.createHash(name + contentToSave.mkString + sgitDirectory)
      save(hash, olderCommit, tree.hash, message, sgitDirectory)
      Some(hash)
    } else {
      None
    }
    //Data about commit
  }

  def prepareContent(
      listOfStageLines: List[String],
      workingDirectory: String,
      sgitDirectory: String
  ): Option[List[List[String]]] = {
    //On doit mettre le path.split + hash
    //val contentCleaned = deleteFilesCheck(listOfStageLines, repoDirectory, sgitDirectory)
    if (listOfStageLines.head.isEmpty) {
      None
    } else {
      Some(
        listOfStageLines
          .map(elem => transformPathLine(elem.split(s" ${Stage.separator} ")))
          .toList
      )
    }
  }

  def deleteFilesCheck(
      listOfStageLines: List[String],
      workingDirectory: String,
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
    /**
      * line 1 : parent Commit
      * line 2 : parent tree
      * line 3 : (optionnal) message
      */
    val content =
      s"oldCommit : $olderCommit\ntrees: $tree\nmessage: $message"
    val path = s"${sgitDirectory}${File.separator}commits"
    FileManager.createFile(hash, content, path)
    Log.update(sgitDirectory,s"$hash -- $message\n Author: Jonh Doe\n\n")
  }

  def extractContentLastCommit(sgitDirectory: String): String = {
    val commit = Head.getLastCommit(sgitDirectory)
    FileManager.extractContentFromPath(s"$sgitDirectory/commits/$commit")
  }

  def extractContentFromCommit(sgitDirectory: String, hash: String): String = {
    FileManager.extractContentFromPath(s"$sgitDirectory/commits/$hash")
  }

  def getTree(sgitDirectory: String, commitHash: String): String = {
    val content = FileManager
      .extractContentFromPath(s"$sgitDirectory/commits/$commitHash")
      .split("\n")
    content(1)

  }

}
