import app.command.AddCommand
import org.scalatest._
import app.command.AddCommand
import java.io.File
import app.components.Blobs
import app.components.Sgit
import app.command.Initializer


class AddSpec extends FlatSpec with Matchers {

    val optionWorkingDirectory : Option[String] = Sgit.getRepoPath()
    val optionSgitDirectory : Option[String] = Sgit.getSgitPath()
    if(optionSgitDirectory.isDefined && optionWorkingDirectory.isDefined){
      val workingDirectory : String = optionWorkingDirectory.get
      val sgitDirectory: String = optionSgitDirectory.get
      "The add command" should "receive the path from parser" in {
        val fileToAdd : String = workingDirectory + "/src/test/testEnvironment/file1.txt"
        val add = new AddCommand(fileToAdd, workingDirectory, sgitDirectory)
        assert(!add.fileToAddPath.isEmpty())
      }

      it should "create a blob from the path given in parameter: " in {
        val init = new Initializer()
        init.initialise
        val hashedValue = Blobs.createBlob("/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/file1.txt",sgitDirectory)
        assert(hashedValue.isDefined)
        assert(new File(s"$sgitDirectory/blobs/"+hashedValue.get).exists)
      }
    }



}  