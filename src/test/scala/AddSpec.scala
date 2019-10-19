import app.command.AddCommand
import org.scalatest._
import java.io.File
import app.components.{Blobs, Sgit, FileManager, Stage}
import app.command.Initializer

class AddSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise
    // Fait le test
    try test()
    finally {
      val repo = Sgit.getRepoPath()
      val sgitDirectory = Sgit.getSgitPath()
      if (sgitDirectory.isDefined) {
        if (new File(s"$repo/.sgit").exists())
          FileManager.delete(s"$repo/.sgit")
      }
    }
  }

  "The add command" should "receive the path from parser" in {

    val workingDirectory: String = Sgit.getRepoPath().get
    val sgitDirectory: String = Sgit.getSgitPath().get

    val fileToAdd: String = "/src/test/testEnvironment/file1.txt"
    val fileToAdd2: String = "/src/test/testEnvironment/file3.txt"
    val blob = Blobs(sgitDirectory, workingDirectory, fileToAdd)
    val stage = Stage(sgitDirectory, workingDirectory)
    val add = AddCommand(sgitDirectory, workingDirectory)
    val newStage = add.addToStage(fileToAdd, stage, blob)
    val blob2 = Blobs(sgitDirectory, workingDirectory, fileToAdd2)
    val add2 = AddCommand(sgitDirectory, workingDirectory)
    val lastStage = add2.addToStage(fileToAdd2, newStage, blob2)
    assert(lastStage.contains(fileToAdd) && lastStage.contains(fileToAdd2))
  }

}
