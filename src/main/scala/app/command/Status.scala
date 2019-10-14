package app.command
import app.components.Stage
import app.components.FileManager

object Status {
  def getStatus(sgitDirectory: String, workingDirectory: String): String = {
    val changesToCommit = getchangesToBeCommited(sgitDirectory)
    val untrackedFiles = getUntrackedFiles(sgitDirectory, workingDirectory)
    val filesStagedButEdited =
      getFilesStagedButEdited(sgitDirectory, workingDirectory)
    s"""
    ${Console.YELLOW}
    \rEdited files :
      $filesStagedButEdited
    ${Console.RESET}
    ${Console.GREEN}
    \rNew files : 
    $changesToCommit
    ${Console.RESET}
    ${Console.RED}
    \rUntracked files :\n
    $untrackedFiles
    ${Console.RESET}
    """
  }

  // Return all the files in repo but not in stage (all - stage)
  def getUntrackedFiles(
      sgitDirectory: String,
      workingDirectory: String
  ): String = {
    val allFiles =
      FileManager.getAllFilesFromPath(workingDirectory, workingDirectory)
    val stageContent = Stage.getContent(sgitDirectory)
    val res = allFiles.filter(file => !Stage.contains(file, sgitDirectory))
    res.map(elem => s"-  $elem \n").mkString
  }

  // Get all the files that weren't in the previous commit but added now
  def getchangesToBeCommited(sgitDirectory: String): String = {
    // Get all the files added between two commits
    val currentStage = Stage.getAllPath(sgitDirectory)
    val oldStagePath = Stage.getOldStage(sgitDirectory)
    val newFiles = currentStage.filter(path => !oldStagePath.contains(path))
    newFiles
      .filter(path => path.size > 0)
      .map(path => s"-  $path\n")
      .mkString
  }

  // Get all the files that are in stage but their "optentially hash" is different from now
  def getFilesStagedButEdited(
      sgitDirectory: String,
      workingDirectory: String
  ): String = {
    val stage = Stage.getContent(sgitDirectory)
    val paths = Stage.getAllPath(sgitDirectory)
    val tuples = paths.map { path =>
      (
        path,
        FileManager.createHash(
          FileManager.extractContentFromPath(workingDirectory + path)
        )
      )
    }
    val filesEdited = tuples
      .filter(
        elem => !stage.contains(s"${elem._2} ${Stage.separator} ${elem._1}")
      )
      .filter(elem => elem._1.size > 0)
    filesEdited.map(elem => s"-  ${elem._1}\n").mkString
  }

  // Return stage content
  def getTrackedFiles(sgitDirectory: String): List[String] = {
    // SUBSTAIN HASH !!
    Stage.readStage(sgitDirectory).split("\n").toList
  }

}
