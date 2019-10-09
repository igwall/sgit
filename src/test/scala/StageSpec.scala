import app.command.AddCommand
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
      println(s"Repo folder = $repoFolder")
      println(s".sgit path = $sgitFolder")
      val blob = Blobs.createBlob("/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/file1.txt",sgitFolder)
      if(blob.isDefined){
        println(blob.get)
        Stage.addElement(blob.get, s"$repoFolder/src/test/testEnvironment/file1.txt", sgitFolder)
        val stage = new File(sgitFolder + "PATH")
        assert(!FileManager.extractContentFromPath(sgitFolder + "/STAGE").isEmpty())
      }
    }
    it should  "edit the hash if the file is edited then added" in {
      //We edit the file1.txt to check that we could update the hash
      val init = new Initializer()
      init.initialise
      val sgitFolder = Sgit.getSgitPath().get
      val repoFolder = Sgit.getRepoPath().get
      println(s"Repo folder = $repoFolder")
      println(s".sgit path = $sgitFolder")
      FileManager.delete("/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/file1.txt")
      FileManager.createFile("file1.txt", "I'm not tired", "/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/")
      val blob = Blobs.createBlob("/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/file1.txt",sgitFolder)
      if(blob.isDefined){
        println(blob.get)
        Stage.addElement(blob.get, s"$repoFolder/src/test/testEnvironment/file1.txt", sgitFolder)
        val stage = new File(sgitFolder + "PATH")
        assert(!FileManager.extractContentFromPath(sgitFolder + "/STAGE").isEmpty())
      }
    }
      
}