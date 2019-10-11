import org.scalatest._
import java.io.File
import app.command.Initializer
import app.components.{FileManager, Sgit}


class FileManagerSpec extends FlatSpec with Matchers {
  override def withFixture(test: NoArgTest) = {
    try test()
    finally {
      if (new File("/.sgit").exists()) FileManager.delete("/.sgit")
    }
  }
  val initializer = new Initializer()
  initializer.initialise

    "The FileManager" should "create a file with createFile(name, content, path)" in {
      val repoDirectory = Sgit.getRepoPath().get
      val path = s"$repoDirectory/src/test/testEnvironment/"
      val fileName = "file3.txt"
      val content = "test123test"
      FileManager.createFile(fileName,content,path)
      val contentExtracted = FileManager.extractContentFromPath(path+fileName)
      assert(!contentExtracted.isEmpty)
    }

}  