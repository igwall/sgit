import java.io.File

import app.command.Initializer
import app.components.Sgit
import app.components.Blobs
import app.components.FileManager
import org.scalatest._

class BlobSpec extends FlatSpec with Matchers {

    override def withFixture(test: NoArgTest) = {
        try test()
        finally {
            if (new File("/.sgit").exists()) FileManager.delete("/.sgit")
        }
    }
    val initializer = new Initializer()
    initializer.initialise

    "The Blob" should "create a hash in depend of the content of file" in {
        val hash1 : String = FileManager.createHash("toto")
        val hash2 : String = FileManager.createHash("toto")
        assert(hash1.equals(hash2))
    }
    it should "register in blob folder" in {
        val optionSgitFolder = Sgit.getSgitPath()
        if(optionSgitFolder.isDefined){
            val blob = Blobs.createBlob(s"$optionSgitFolder/src/test/testEnvironment/file1.txt",optionSgitFolder.get)
            assert(blob.isDefined)
            assert(!blob.get.isEmpty)
        }

    }
}  