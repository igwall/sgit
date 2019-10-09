package example
import java.io.File
import java.nio.file.Paths
import org.scalatest._
import app.command.Initializer
import app.components.FileManager

class InitializerSpec extends FlatSpec with Matchers {

  //override def withFixture(test: NoArgTest) = {
  //  try test()
  //  finally {
  //    if (new File("../.sgit").exists()) FileManager.delete("../.sgit")
  //    if (new File(".sgit").exists()) FileManager.delete(".sgit")
  //  }
  // }

  "The initializer" should "create a .sgit folder with folders" in {
    val initializer = new Initializer()
    assert(initializer.initialise)
  }
  
  it should "not re-create a project folder if is already existing in path" in {
    val initializer = new Initializer()
    initializer.initialise
    assert(!initializer.initialise)
  }
}
