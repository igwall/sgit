import java.io.File

import app.command.Initializer
import app.components.Sgit
import app.components.Blobs
import app.components.FileManager
import org.scalatest._

class BlobSpec extends FlatSpec with Matchers {


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
        s"$repoFolder/src/test/testEnvironment/file1.txt", optionSgitFolder.get)
      assert(blob.isDefined)
      assert(!blob.get.isEmpty())
    }

  }
}
