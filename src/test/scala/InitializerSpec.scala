package example
import java.io.File
import java.nio.file.Paths
import org.scalatest._
import app.Initializer

class InitializerSpec extends FlatSpec with Matchers {



  "The initializer" should "create a .sgit folder with folders" in {
    val initializer = new Initializer()
    assert(initializer.initialise)
  }
  
  it should "not re-create a project folder if is already existing in path" in {
    val initializer = new Initializer()
    initializer.initialise
    assert(!initializer.initialise)
  }

  it should "Find the .sgit folder in path: " + Paths.get("").toAbsolutePath().toString() in {
    val path = Paths.get("").toAbsolutePath().toString()
    val sgitPath = path + File.separator + ".sgit"
    val sgitFile = new File(sgitPath).exists
    new File(sgitPath).delete()
    assert(sgitFile)
    
  }
}
