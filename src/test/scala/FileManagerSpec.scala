import org.scalatest._
import java.io.File
import app.command.Initializer
import app.components.{FileManager, Sgit}

class FileManagerSpec extends FlatSpec with Matchers {

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
  }

}
