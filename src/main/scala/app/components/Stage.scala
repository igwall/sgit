package app.components
import java.io.File
object Stage {
  // If the path is not written, add it to file
  val separator = "::"
  // Else, update the content
  def addElement(
      hash: String,
      path: String,
      sgitDirectory: String,
      repoDirectory: String
  ): Option[String] = {
    val fullPath = repoDirectory + path
    val stagePath = sgitDirectory + File.separator + "/STAGE"
    //Check that the STAGE file is at the good place
    if (FileManager.exist(stagePath)) {
      val contentStage = FileManager.extractContentFromPath(stagePath)
      //If our path in already in stage, we only update blob hash
      if (isInStage(path, stagePath)) {
        val newContent = updateHash(hash, path, stagePath)
        writeInStage(stagePath, newContent, sgitDirectory)
        Some("File correctly added")
      } else {
        val newContent = contentStage + s"$hash ${Stage.separator} $path\n"
        writeInStage(stagePath, newContent, sgitDirectory)
        Some("File correctly added")
      }
      //Nothing to do, wrong directory.
    } else {
      None
    }
  }

  // Check if a path given in parameter is already written in the stage
  def isInStage(path: String, stagePath: String): Boolean = {
    val contentOfStage = FileManager.extractContentFromPath(stagePath)
    contentOfStage.contains(path)
  }

  // Function that take a path in argument and a hash.
  // Check if the hash is the same for the path given in parameter.
  // If it is, replace the hash, else do nothing
  def updateHash(hash: String, path: String, stagePath: String): String = {
    //We check that the path is correctly staged before editing it
    val contentOfStage = FileManager.extractContentFromPath(stagePath)
    contentOfStage
      .split("\n") // => Splitted by line
      .filter(line => !line.contains(path))
      .map(line => line + "\n") // => get all path expect the one that we want to edit
      .mkString + s"$hash ${Stage.separator} $path\n" // => add the new hash with the path to stage
  }

  def writeInStage(
      stagePath: String,
      contentStage: String,
      sgitDirectory: String
  ): Unit = {
    FileManager.delete(stagePath)
    FileManager.createFile("/STAGE", contentStage, sgitDirectory)
  }

  def readStage(sgitDirectory: String): String = {
    return FileManager.extractContentFromPath(s"${sgitDirectory}/STAGE")
  }

  def getPath(sgitDirectory: String): String = {
    s"${sgitDirectory}/STAGE"
  }

  def cleanPath(path: String, projectDirectory: String): String = {
    path drop projectDirectory.size
  }

  def getContent(sgitDirectory: String): String = {
    FileManager
      .extractContentFromPath(Stage.getPath(sgitDirectory))
  }

  def contains(path: String, sgitDirectory: String): Boolean = {
    val content = getContent(sgitDirectory)
    content.contains(path)
  }

  def backup(sgitDirectory: String) {
    val path = s"$sgitDirectory/.old/STAGE.old"
    if (FileManager.exist(path)) {
      FileManager.delete(path)
    }
    FileManager.createFile(
      "STAGE.old",
      Stage.getContent(sgitDirectory),
      s"$sgitDirectory/.old/"
    )
  }

  def getOldStage(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(s"$sgitDirectory/.old/STAGE.old")
  }

  def getAllPath(sgitDirectory: String): List[String] = {
    Stage
      .readStage(sgitDirectory)
      .split("\n")
      .toList
      .flatMap(
        _.split(s" ${Stage.separator} ").toList
          .filter(_.contains("/"))
      )
  }

  def getMappingHash(sgitDirectory: String) = {
    val stage = Stage.getContent(sgitDirectory)
    val stageSplitted = stage.split("\n")
  }
}
