import app.command.AddCommand
import org.scalatest._
import java.io.File
import app.components.Blobs
import app.components.Sgit
import app.command.Initializer
import app.components.FileManager


class AddSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    try test()
    finally {
      if (new File("/.sgit").exists()) FileManager.delete("/.sgit")
    }
  }
  val initializer = new Initializer()
  initializer.initialise
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
        val repoDirectory = Sgit.getRepoPath().get
        val hashedValue = Blobs.createBlob(s"$repoDirectory/src/test/testEnvironment/file1.txt",sgitDirectory)
        assert(hashedValue.isDefined)
        assert(new File(s"$sgitDirectory/blobs/"+hashedValue.get).exists)
      }
    }



}  