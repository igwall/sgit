import app.command.AddCommand
import org.scalatest._
import java.io.File
import app.components.{Blobs, Sgit, FileManager, Stage}
import app.command.Initializer

class AddSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    val initializer = new Initializer()
    initializer.initialise
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

  val optionWorkingDirectory: Option[String] = Sgit.getRepoPath()
  val optionSgitDirectory: Option[String] = Sgit.getSgitPath()
  if (optionSgitDirectory.isDefined && optionWorkingDirectory.isDefined) {
    val workingDirectory: String = optionWorkingDirectory.get
    val sgitDirectory: String = optionSgitDirectory.get

    "The add command" should "receive the path from parser" in {
      val fileToAdd: String = "/src/test/testEnvironment/file1.txt"
      val fileToAdd2: String = "/src/test/testEnvironment/file3.txt"
      val blob = Blobs(sgitDirectory, workingDirectory, fileToAdd)
      val stage = Stage(sgitDirectory, workingDirectory)
      val add = AddCommand(sgitDirectory, workingDirectory)
      val newStage = add.addToStage(fileToAdd, stage, blob)
      assert(newStage.contains(fileToAdd))
    }

    it should "create a blob from the path given in parameter: " in {
      val init = new Initializer()
      init.initialise
      val repoDirectory = Sgit.getRepoPath().get
      val hashedValue = Blobs.createBlob(
        s"$repoDirectory/src/test/testEnvironment/file1.txt",
        sgitDirectory
      )
      assert(hashedValue.isDefined)
      assert(new File(s"$sgitDirectory/blobs/" + hashedValue.get).exists)
    }
  }

}
