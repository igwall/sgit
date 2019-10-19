import org.scalatest._
import java.io.File
import app.command.Initializer
import app.components.{FileManager, Sgit}

class FileManagerSpec extends FlatSpec with Matchers {

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

  "The FileManager" should "create a file with createFile(name, content, path)" in {
    val repoDirectory = Sgit.getRepoPath().get
    val path = s"${repoDirectory}/src/test/testEnvironment/"
    val fileName = "file3.txt"
    val content = "test123test"
    FileManager.createFile(fileName, content, path)
    val contentExtracted = FileManager.extractContentFromPath(path + fileName)
    assert(!contentExtracted.isEmpty)
  }

  it should "return all the files from getAllFilesFromPath with a given path" in {
    val repoPath = Sgit.getRepoPath().get
    val listOfFiles =
      FileManager.getAllFilesFromPath(
        s"$repoPath/src/test/testEnvironment",
        s"$repoPath/src/test/testEnvironment"
      )
    assert(listOfFiles.size == 7)
  }

}
