import org.scalatest._
import app.command.{Diff, Initializer, AddCommand}
import app.components.{Sgit, FileManager, Blobs, Stage}
import java.io.File

class DiffSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    val initializer = new Initializer()
    initializer.initialise

    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    val specialPath = s"$workingDirectory/src/test/testEnvironment"
    FileManager.update("newFile.txt", "i'm content", specialPath)

    val fileToAdd: String = "/src/test/testEnvironment/newFile.txt"
    val blob = Blobs(sgitDirectory, workingDirectory, fileToAdd)
    blob.save()
    val stage = Stage(sgitDirectory, workingDirectory)
    val add = AddCommand(sgitDirectory, workingDirectory)
    val newStage = add.addToStage(fileToAdd, stage, blob)
    newStage.save()
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

  it should "create the matrix of content" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    val file1 = List("a", "b", "c", "d", "e")
    val file2 = List("a", "c", "f", "e")
    val diff: Diff = Diff(sgitDirectory, workingDirectory)
    val res = diff.makeDiff(file1, file2)
    assert(!res.isEmpty())
  }

  it should "get the diff from stage and current working tree" in {

    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    val specialPath = s"$workingDirectory/src/test/testEnvironment"
    FileManager.update("newFile.txt", "i'm not a secret content", specialPath)
    val diff = Diff(sgitDirectory, workingDirectory)
    val res = diff.listOfDiff()
    assert(!res.isEmpty)
  }

}
