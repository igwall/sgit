import org.scalatest._
import app.components.Sgit
import app.components.{FileManager, Stage, Blobs}
import app.command.Status
import app.command.{AddCommand, Initializer}
import java.io.File
import app.command.Commit

class Status extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {

    val initializer = new Initializer()
    initializer.initialise

    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    FileManager.update(
      "file1.txt",
      "igwall is here",
      s"$workingDirectory/src/test/testEnvironment"
    )

    val fileToAdd: String = "/src/test/testEnvironment/file1.txt"
    val blob = Blobs(sgitDirectory, workingDirectory, fileToAdd)
    val stage = Stage(sgitDirectory, workingDirectory)
    val add = AddCommand(sgitDirectory, workingDirectory)
    val newStage = add.addToStage(fileToAdd, stage, blob)
    newStage.save()

    val commit: Commit = Commit(sgitDirectory, workingDirectory, "i'm a commit")
    commit.save()

    FileManager.update(
      "file1.txt",
      "igwall is encore here",
      s"$workingDirectory/src/test/testEnvironment"
    )
    try test()
    finally {
      val repo = Sgit.getRepoPath().get
      if (new File(s"$repo/.sgit").exists()) FileManager.delete(s"$repo/.sgit")
    }
  }

  "Status" should "found untracked files: " in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    val specialPath = s"$workingDirectory/src/test/testEnvironment"
    val status = Status(sgitDirectory, specialPath)
    val untrackedFiles = status.getUntrackedFiles()
    assert(!untrackedFiles.isEmpty())
  }

  it should "find all the files added between two commits" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    val specialPath = s"$workingDirectory/src/test/testEnvironment"

    val fileToAdd: String = "/src/test/testEnvironment/newFile.txt"
    val blob = Blobs(sgitDirectory, workingDirectory, fileToAdd)
    val stage = Stage(sgitDirectory, workingDirectory)
    val add = AddCommand(sgitDirectory, workingDirectory)
    val newStage = add.addToStage(fileToAdd, stage, blob)
    newStage.save()

    val status =
      Status(sgitDirectory, specialPath)
    val changesToBeCommited = status.getchangesToBeCommited()
    assert(changesToBeCommited == "-  /src/test/testEnvironment/newFile.txt\n")
  }

  it should "find an updated file" in {
    val workingDirectory = Sgit.getRepoPath().get
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.update(
      "file1.txt",
      "igwall is back",
      s"$workingDirectory/src/test/testEnvironment"
    )
    val status =
      Status(sgitDirectory, s"$workingDirectory/src/test/testEnvironment/")
    assert(
      !status.getFilesStagedButEdited().isEmpty
    )
  }

}
