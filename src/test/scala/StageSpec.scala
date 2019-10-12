import org.scalatest._
import app.components.Stage
import java.io.File
import app.components.FileManager
import app.components.Sgit
import app.command.Initializer
import app.components.Blobs

class StageSpec extends FlatSpec with Matchers {


  "Stage" should "add line into itself" in {
    val init = new Initializer()
    init.initialise
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get
    val blob = Blobs.createBlob(
      s"$repoFolder/src/test/testEnvironment/file1.txt",
      sgitFolder
    )
    if (blob.isDefined) {
      Stage.addElement(
        blob.get,
        s"$repoFolder/src/test/testEnvironment/file1.txt",
        sgitFolder,
        repoFolder
      )
      val stage = new File(sgitFolder + "PATH")
      assert(
        !FileManager.extractContentFromPath(sgitFolder + "/STAGE").isEmpty()
      )
    }
  }
  it should "edit the hash if the file is edited then added" in {
    //We edit the file1.txt to check that we could update the hash
    val init = new Initializer()
    init.initialise
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get
    FileManager.delete(
      s"$repoFolder/src/test/testEnvironment/file1.txt"
    )
    FileManager.createFile(
      "file1.txt",
      "I'm not tired",
      s"$repoFolder/src/test/testEnvironment/"
    )
    val blob = Blobs.createBlob(
      s"$repoFolder/src/test/testEnvironment/file1.txt",
      sgitFolder
    )
    if (blob.isDefined) {
      Stage.addElement(
        blob.get,
        s"$repoFolder/src/test/testEnvironment/file1.txt",
        sgitFolder,
        repoFolder
      )
      val stage = new File(sgitFolder + "PATH")
      assert(
        !FileManager.extractContentFromPath(sgitFolder + "/STAGE").isEmpty()
      )
    }
  }

  it should "clean the path" in {
    val repoFolder = Sgit.getRepoPath().get
    val path = s"$repoFolder/5/6/monfichier.txt"
    val cleanedPath = Stage.cleanPath(path, repoFolder)
    assert(cleanedPath == "/5/6/monfichier.txt")
  }
}
