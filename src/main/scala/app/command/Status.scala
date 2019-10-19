package app.command
import app.components.{Stage, Blobs, FileManager}

case class Status(
    stage: Stage,
    // (path, fakeBlob) => fakeBlob is a blob from real content and not the one in stage
    allFilesPathContentBlob: List[(String, Blobs)]
) {

  def getUntrackedFiles(): String = {
    val res = allFilesPathContentBlob.filter(file => !stage.contains(file._1))
    res.filter(line => line != "").map(elem => s"-  $elem \n").mkString
  }

  def getchangesToBeCommited(): String = {
    // Get all the files added between two commits
    stage
      .newFiles()
      .filter(path => path.size > 0)
      .map(path => s"-  $path\n")
      .mkString
  }

  def getFilesStagedButEdited(): String = {
    val filtered = allFilesPathContentBlob
      .filter(
        elem =>
          !stage
            .contains(s"${elem._2.hash} ${stage.separator} ${elem._1}")
      )
      .filter(elem => elem._1.size > 0)
    filtered.map(elem => s"-  ${elem._1}\n").mkString
  }

  def getStatus(): String = {
    val changesToCommit = getchangesToBeCommited()
    val untrackedFiles = getUntrackedFiles()
    val filesStagedButEdited =
      getFilesStagedButEdited()
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

}
object Status {
  def apply(sgitDirectory: String, workingDirectory: String): Status = {
    val stage: Stage = Stage(sgitDirectory, workingDirectory)
    val allFilesPaths =
      FileManager.getAllFilesFromPath(workingDirectory, workingDirectory)

    val allFilesPathsAndContent = allFilesPaths.map(
      path =>
        (
          path,
          Blobs(sgitDirectory, workingDirectory, workingDirectory + path)
        )
    )
    new Status(stage, allFilesPathsAndContent)
  }

}
