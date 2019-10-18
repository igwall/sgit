package app.command
import app.components.Blobs
import app.components.Stage
import app.components.{FileManager, Stage}
import java.io.File

case class AddCommand(
    val workingDirectory: String,
    val sgitDirectory: String,
) {

  def addToStage(file: String, stage: Stage, blob: Blobs): Stage = {
    // Check if the file that we want to add is in the same path than the .sgit project
    //If the blob is correctly created
    stage.addElement(
      blob.hash,
      file,
      sgitDirectory,
      workingDirectory
    )

  }
}

object AddCommand {

  def apply(
      sgitDirectory: String,
      repoDirectory: String,
  ): AddCommand = {
    new AddCommand(repoDirectory, sgitDirectory)
  }

}
