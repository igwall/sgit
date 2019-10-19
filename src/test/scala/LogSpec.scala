package example
import java.io.File
import org.scalatest._
import app.components.Sgit
import app.command.{Initializer}
import app.components.{FileManager}
import app.components.Log

class LogSpec extends FlatSpec with Matchers {

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

  "The log" should "be updated after a commit" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val log = Log(sgitDirectory)
    val res = log.update("NEW CONTENT")
    res.save()
    val logCHeck = Log(sgitDirectory)
    assert(logCHeck.content.contains("NEW CONTENT"))
  }
}
