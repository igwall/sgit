import org.scalatest._
import app.components.Stage
import java.io.File
import app.components.FileManager
import app.components.Sgit
import app.command.Initializer
import app.components.Blobs

class StageSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    val initializer = new Initializer()
    initializer.initialise
    try test()
    finally {
      val repo = Sgit.getRepoPath().get
      if (new File(s"$repo/.sgit").exists()) FileManager.delete(s"$repo/.sgit")
    }
  }

  "Stage" should "add line into itself" in {
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get
    val blob = Blobs.createBlob(
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder
    )
    if (blob.isDefined) {
      Stage.addElement(
        blob.get,
        s"${repoFolder}/src/test/testEnvironment/file1.txt",
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
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get

    Stage.addElement(
      "aaaa",
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder,
      repoFolder
    )

    Stage.addElement(
      "bbbb",
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder,
      repoFolder
    )
    val res = FileManager
      .extractContentFromPath(s"${sgitFolder}${File.separator}STAGE")
      .split("\n")
      .size == 1
    assert(
      res
    )
  }

  it should "clean the path" in {
    val repoFolder = Sgit.getRepoPath().get
    val path = s"${repoFolder}/5/6/monfichier.txt"
    val cleanedPath = Stage.cleanPath(path, repoFolder)
    assert(cleanedPath == "/5/6/monfichier.txt")
  }

  it should "get all the paths" in {
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get
    Stage.addElement(
      "bbbb",
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder,
      repoFolder
    )
    Stage.addElement(
      "aaaa",
      s"${repoFolder}/src/test/testEnvironment/file2.txt",
      sgitFolder,
      repoFolder
    )
    val paths = Stage.getAllPath(sgitFolder)
    assert(paths.size == 2)
  }

}
