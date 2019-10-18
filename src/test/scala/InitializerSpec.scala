package example
import java.io.File
import org.scalatest._
import app.components.Sgit
import app.command.Initializer
import app.components.FileManager

class InitializerSpec extends FlatSpec with Matchers {

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

  "The initializer" should "not  create a .sgit folder with folders" in {
    val initializer = new Initializer()
    assert(!initializer.initialise.isDefined)
  }
}
