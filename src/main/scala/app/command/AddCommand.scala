package app.command
import app.components.Blobs
import app.components.Stage
import app.components.FileManager
import java.io.File

class AddCommand(
    val fileToAddPath: String = "",
    val workingDirectory: String,
    val sgitDirectory: String
) {

  def addToStage(): Option[String] = {
    // Check if the file that we want to add is in the same path than the .sgit project
    val canonicalPath = workingDirectory + File.separator + fileToAddPath
    if (FileManager.exist(canonicalPath)) {
      val blobHash: Option[String] =
        Blobs.createBlob(canonicalPath, sgitDirectory)
      //If the blob is correctly created
      if (blobHash.isDefined) {
        Stage.addElement(
          blobHash.get,
          fileToAddPath,
          sgitDirectory,
          workingDirectory
        )
        return Some("OK")
      } else {
        return None
      }
    } else {
      None
    }
  }

}
