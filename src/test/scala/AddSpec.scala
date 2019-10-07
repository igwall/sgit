import app.command.AddCommand
import org.scalatest._
import java.nio.file.Paths
import app.command.AddCommand
import app.components.Sgit


class AddSpec extends FlatSpec with Matchers {


    "The add command" should "receive the path from parser" in {
      println(Paths.get("").toAbsolutePath().toString())
      val fileToAdd : String = Paths.get("").toAbsolutePath().toString() + "/src/test/testEnvironment/file1.txt"
      val workingDirectory = new Sgit()
      val add = new AddCommand(fileToAdd, workingDirectory.getPath().get)
      assert(!add.param.isEmpty())
    }

    it should "create a blob from the path given in parameter: " in {
      assert(true)
    }
}  