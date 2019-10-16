import java.io.File

import app.command.Initializer
import app.components.Sgit
import app.components.Blobs
import app.components.FileManager
import org.scalatest._

class BlobSpec extends FlatSpec with Matchers {

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

  "The Blob" should "create a hash in depend of the content of file" in {
    val hash1: String = FileManager.createHash("toto")
    val hash2: String = FileManager.createHash("toto")
    assert(hash1.equals(hash2))
  }
  it should "register in blob folder" in {
    val optionSgitFolder = Sgit.getSgitPath()
    val repoFolder = Sgit.getRepoPath().get
    if (optionSgitFolder.isDefined) {
      val blob = Blobs.createBlob(
        s"$repoFolder/src/test/testEnvironment/file1.txt",
        optionSgitFolder.get
      )
      assert(blob.isDefined)
      assert(!blob.get.isEmpty())
    }

  }
}
