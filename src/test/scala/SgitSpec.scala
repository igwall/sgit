import app.command.AddCommand
import org.scalatest._
import java.nio.file.Paths
import app.components.Sgit

class SgitSpec extends FlatSpec with Matchers {


    "The sgit.getPath" should "return the path of the .sgit of the project" in {
        val sgitManager = new Sgit()
        val path : Option[String] = sgitManager.getPath()
        assert(path.isDefined)
        assert(path.get == "/Users/lucasgoncalves/Git/sgit/.sgit" )
    }
}  