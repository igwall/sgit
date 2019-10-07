import org.scalatest._
import java.io.File
import app.components.FileManager


class FileManagerSpec extends FlatSpec with Matchers {


    "The FileManager" should "searchFolder() and return the good result" in {
      val file : File = new File(".").getCanonicalFile()
      val folder = ".sgit/"
      val sgitPath = FileManager.searchFolder(file,folder)
      assert(sgitPath.isDefined)
      assert(sgitPath.get == "/Users/lucasgoncalves/Git/sgit/.sgit")
    } 
    
    it should "extract the content with extractContentFromFile()" in {
        val path = "/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/file1.txt"
        val content = FileManager.extractContentFromPath(path)
        assert(content == "Hey, I'm a test")
    }


}  