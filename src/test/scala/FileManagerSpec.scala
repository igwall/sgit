import org.scalatest._
import java.io.File
import app.components.FileManager


class FileManagerSpec extends FlatSpec with Matchers {


    "The FileManager" should "create a file with createFile(name, content, path)" in {
      val path = "/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/"
      val fileName = "file3.txt"
      val content = "test123test"
      FileManager.createFile(fileName,content,path)
      val contentExtracted = FileManager.extractContentFromPath(path+fileName)
      assert(!contentExtracted.isEmpty)
    }

}  