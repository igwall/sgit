import org.scalatest._
import app.components.Sgit
import app.components.FileManager
import app.command.Status

class Status extends FlatSpec with Matchers {

  val sgitDirectory = Sgit.getSgitPath().get
  val workingDirectory = Sgit.getRepoPath().get
  "Status" should "found untracked files: " in {
    val files = Status.getUntrackedFiles(
      sgitDirectory,
      s"$workingDirectory/src/test/testEnvironment"
    )
    assert(!files.isEmpty())
  }
  it should "find an updated file" in {
    val workingDirectory = Sgit.getRepoPath().get
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.update(
      "file1.txt",
      "igwall is here",
      s"$workingDirectory/src/test/testEnvironment"
    )
    assert(
      !Status.getFilesStagedButEdited(sgitDirectory, workingDirectory).isEmpty
    )
  }

}
