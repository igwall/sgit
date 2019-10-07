import app.command.AddCommand
import org.scalatest._

class BlobSpec extends FlatSpec with Matchers {


    "The Blob"  should "create a hash in depend of the content of file" in {
        val hash1 : String = Blob.createHash("toto")
        val hash2 : String = Blob.createHash("toto")
        assert(hash1.equals(hash2))
    }
}  