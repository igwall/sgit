import org.scalatest._
import app.components.Sgit
import app.components.{FileManager, Stage}
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

    val add = new AddCommand(
      s"$sgitDirectory/src/test/testEnvironment/file1.txt",
      workingDirectory,
      sgitDirectory
    )
    add.addToStage()

    Commit.create(sgitDirectory, "status", workingDirectory)

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
    val files = Status.getUntrackedFiles(
      sgitDirectory,
      s"$workingDirectory/src/test/testEnvironment"
    )
    assert(!files.isEmpty())
  }

  it should "find all the files added between two commits" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    FileManager.createFile(
      "test2.txt",
      "hello, thanks to read this",
      s"$workingDirectory/src/test/testEnvironment"
    )
    Stage.addElement(
      "jfkyf",
      "/src/test/testEnvironment/file2.txt",
      sgitDirectory,
      workingDirectory
    )
    val res = Status.getchangesToBeCommited(
      s"$sgitDirectory"
    )
    assert(res == "-  /src/test/testEnvironment/file2.txt\n")
  }

/*
  it should "find an updated file" in {
    val workingDirectory = Sgit.getRepoPath().get
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.update(
      "file1.txt",
      "igwall is back",
      s"$workingDirectory/src/test/testEnvironment"
    )
    assert(
      !Status.getFilesStagedButEdited(sgitDirectory, workingDirectory).isEmpty
    )
  }
  */

}
