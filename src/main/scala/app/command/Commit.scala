package app.command
import app.components.Stage
import app.components.Tree
import app.command.Status
import app.components.{FileManager, Log, Head}
import java.io.File

case class Commit(
    sgitDirectory: String,
    workingDirectory: String,
    stage: Stage,
    log: Log,
    head: Head,
    message: String
) {

  def save(): Option[String] = {
    //Prepare all the trees to save

    val contentToSave =
      prepareContent(stage.contentByLines)
    if (contentToSave.isDefined) {
      val name = ""
      val olderCommit: String = head.content
      val tree = Tree.createTree(name, contentToSave.get, sgitDirectory)
      val hash =
        // Not an I/O => RT Function createHash
        FileManager.createHash(name + contentToSave.mkString + sgitDirectory)

      val content =
        s"oldCommit:$olderCommit\ntrees:$tree\nmessage:$message"
      val path = s"${sgitDirectory}/commits"
      FileManager.createFile(hash, content, path)

      //Save new commit hash on HEAD
      head.update(hash, sgitDirectory)

      // Save new version of Log  :
      val newlog =
        log.update(s"$hash -- $message\n Author: Jonh Doe\n\n")
      newlog.save()

      Some(hash)
    } else {
      None
    }
    //Data about commit
  }

  def prepareContent(
      listOfStageLines: List[String]
  ): Option[List[List[String]]] = {
    //On doit mettre le path.split + hash
    //val contentCleaned = deleteFilesCheck(listOfStageLines, repoDirectory, sgitDirectory)
    if (listOfStageLines.head.isEmpty) {
      None
    } else {
      Some(
        listOfStageLines
          .map(elem => transformPathLine(elem.split(s" ${stage.separator} ")))
          .toList
      )
    }
  }

  def transformPathLine(line: Array[String]): List[String] = {
    val pathSplitted = line(1).split("/").toList
    val cleanedPathSplitted = pathSplitted.filter(path => path.size > 0)
    cleanedPathSplitted.dropRight(1) :+ line(0) //We replace the name of the file with is hash
  }

  /*
  def deleteFilesCheck(
      listOfStageLines: List[String]
  ): List[String] = {
    val deleteBlankLine = listOfStageLines.filter(line => line != "")
    val file = deleteBlankLine.map { line =>
      val split = line.split(s" ${stage.separator} ")
      println(split(0) + "," + split(1))
      (split(0), split(1))
    }
    deleteTraitment(sgitDirectory, file)
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
 */

}
object Commit {

  def apply(
      sgitDirectory: String,
      workingDirectory: String,
      message: String
  ): Commit = {
    val stage = Stage(sgitDirectory, workingDirectory)
    val log = Log(sgitDirectory)
    val head = Head(sgitDirectory)
    new Commit(sgitDirectory, workingDirectory, stage, log, head, message)
  }

  def extractContentLastCommit(sgitDirectory: String): String = {
    val commit = FileManager.extractContentFromPath(s"$sgitDirectory/HEAD")
    FileManager.extractContentFromPath(s"$sgitDirectory/commits/$commit")
  }

  def extractContentFromCommit(sgitDirectory: String, hash: String): String = {
    FileManager.extractContentFromPath(s"$sgitDirectory/commits/$hash")
  }

  def getTree(sgitDirectory: String, commitHash: String): String = {
    FileManager
      .extractContentFromPath(s"$sgitDirectory/commits/$commitHash")
      .split("\n")(1)
      .split(":")(1)
  }

}
