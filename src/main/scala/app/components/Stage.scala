package app.components
import java.io.File
import scala.annotation.tailrec
import app.command.Commit
import app.components.{Tree, Blobs}

case class Stage(
    sgitDirectory: String,
    workingDirectory: String,
    content: String,
    oldContent: String
) {

  val separator = "::"
  val contentByLines = content.split("\n").toList

  // Return a new stage edited
  def addElement(
      hash: String,
      path: String,
      sgitDirectory: String,
      repoDirectory: String
  ): Stage = {
    val fullpath = s"$repoDirectory/$path"
    //Check that the STAGE path is at the good place
    //If our path in already in stage, we only update blob hash
    if (isInStage(path)) {
      val newContent = updateHash(hash, path)
      new Stage(sgitDirectory, workingDirectory, newContent, oldContent)
    } else {
      val newContent = content + s"$hash ${separator} $path\n"
      new Stage(sgitDirectory, workingDirectory, newContent, oldContent)
    }
  }

  def isInStage(path: String): Boolean = {
    content.contains(path)
  }

  // Function that take a path in argument and a hash.
  // Check if the hash is the same for the path given in parameter.
  // If it is, replace the hash, else do nothing
  def updateHash(hash: String, path: String): String = {
    //We check that the path is correctly staged before editing it
    content
      .split("\n") // => Splitted by line
      .filter(line => !line.contains(path))
      .map(line => line + "\n") // => get all path expect the one that we want to edit
      .mkString + s"$hash ${separator} $path\n" // => add the new hash with the path to stage
  }

  // WRITE FILES
  def save(): Unit = {
    FileManager.delete(getPath(sgitDirectory))
    FileManager.createFile("STAGE", content, sgitDirectory)
    FileManager.delete(s"$sgitDirectory/.old/STAGE.old")
    FileManager.createFile("STAGE.old", oldContent, s"$sgitDirectory/.old")
  }

  def getPath(sgitDirectory: String): String = {
    s"${sgitDirectory}${File.separator}STAGE"
  }

  def cleanPath(path: String, projectDirectory: String): String = {
    path drop projectDirectory.size
  }

  def delete(path: String): String = {
    content
      .split("\n") // => Splitted by line
      .filter(line => !line.contains(path))
      .map(line => line + "\n") // => get all path expect the one that we want to edit
      .mkString
  }

  def getAllBlobs(): List[String] = {
    content
      .split("\n")
      .toList
      .flatMap(
        _.split(s" ${separator} ").toList
          .filter(!_.contains("/"))
      )
  }

  // Check that the path given is in stage
  def contains(path: String): Boolean = {
    content.contains(path)
  }

  def getAllPath(): List[String] = {
    content
      .split("\n")
      .toList
      .flatMap(
        _.split(s" ${separator} ").toList
          .filter(_.contains("/"))
      )
  }

  def getTuplesHashPath(): List[(String, String)] = {
    val blobs = getAllBlobs()
    val paths = getAllPath()

    @tailrec
    def createTuple(
        blobs: List[String],
        paths: List[String],
        res: List[(String, String)]
    ): List[(String, String)] = {
      if (blobs.tail.isEmpty || paths.isEmpty) {
        res
      } else {
        val newRes = (blobs.head, paths.head) +: res
        createTuple(blobs.tail, paths.tail, newRes)
      }
    }
    createTuple(blobs, paths, List())
  }

}
object Stage {

  // I/O => Get content from files
  def apply(sgitDirectory: String, workingDirectory: String): Stage = {
    val content = FileManager.extractContentFromPath(s"$sgitDirectory/STAGE")
    val oldContent =
      FileManager.extractContentFromPath(s"$sgitDirectory/.old/STAGE.old")
    new Stage(sgitDirectory, workingDirectory, content, oldContent)
  }

  /*
  def createStageFromMainTree(
      sgitDirectory: String,
      mainTree: String
  ): String = {

    def createStage(
        currentPath: String,
        currentStageContent: String,
        trees: List[String]
    ): String = {
      trees.map { tree =>
        val contentOfTree = extractFromHash(sgitDirectory, tree)
        val path = contentOfTree._1
        val trees = contentOfTree._2.map(_.trim).filterNot(_ == "")
        val blobs = contentOfTree._3.map(_.trim).filterNot(_ == "")
        val content = blobs.map { blob =>
          if (currentPath == "") {
            blob + " :: " + path + File.separator + Blobs
              .extractName(sgitDirectory, blob)
          } else {
            blob + " :: " + currentPath + File.separator + path + File.separator + Blobs
              .extractName(sgitDirectory, blob)
          }

        }.mkString
        if (trees.nonEmpty) {
          if (currentPath == "") {
            createStage(
              path,
              currentStageContent + content,
              trees
            )
          } else {
            createStage(
              currentPath + File.separator + path,
              currentStageContent + content,
              trees
            )
          }
        } else {
          currentStageContent + content
        }
      }.mkString
    }
    // Get the tree from commit Content
    createStage("", "", List(mainTree))
  }


  // I/O Function => Delete real files
  def deleteFilesInStage(sgitDirectory: String, repoDirectory: String) = {
    val paths = getAllPath(sgitDirectory)
    println(s"files to delete = ${paths.mkString}")
    paths.map(path => FileManager.delete(s"${repoDirectory}$path"))
  }

  def extractFromHash(
      sgitDirectory: String,
      hash: String
  ): (String, List[String], List[String]) = {
    val content =
      FileManager.extractContentFromPath(sgitDirectory + "/trees/" + hash)
    val contentSplitted = content.split("\n")
    val contentCleaned = contentSplitted.map(line => line drop 2)
    val name = contentCleaned(0)
    val trees = contentCleaned(1).split(",").toList
    val blobs = contentCleaned(2).split(",").toList
    (name, trees, blobs)
  }

  def importStage(sgitDirectory: String, stage: String) {
    // Write the new content in stage file
    FileManager.delete(s"$sgitDirectory/STAGE")
    FileManager.createFile("STAGE", stage, sgitDirectory)
    println(stage)

    //Change the content of all files staged :
    val contentSplitted = getTuplesHashPath(sgitDirectory)
    //1. Delete the file
    //2. For each line in path :
    contentSplitted.map { file =>
      val blobContent =
        Blobs.getContent(file._1, sgitDirectory).split("\n").toList
      FileManager.createFile(
        blobContent.head,
        blobContent.tail.mkString,
        sgitDirectory
      )
    }
    //Get the blob, get the path,
    //Extract content from blob
    //Create file with blob content
  }
 */
}
