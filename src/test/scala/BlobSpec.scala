import app.command.AddCommand
import app.components.Sgit
import app.components.Blobs
import org.scalatest._

class BlobSpec extends FlatSpec with Matchers {


    "The Blob" should "create a hash in depend of the content of file" in {
        val hash1 : String = Blobs.createHash("toto")
        val hash2 : String = Blobs.createHash("toto")
        assert(hash1.equals(hash2))
    }
    it should "register in blob folder" in {
        val optionSgitFolder = Sgit.getSgitPath()
        if(optionSgitFolder.isDefined){
            val blob = Blobs.createBlob("/Users/lucasgoncalves/Git/sgit/src/test/testEnvironment/file1.txt",optionSgitFolder.get)
            assert(blob.isDefined)
            assert(!blob.get.isEmpty)
        }

    }
}  