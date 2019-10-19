import java.io.File

import app.command.{Initializer}
import org.scalatest._
import app.components.{FileManager, Sgit}

class SgitSpec extends FlatSpec with Matchers {

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

  "The sgit.getSgitPath()" should "return the path of the .sgit of the project" in {
    val optionSgitPath: Option[String] = Sgit.getSgitPath()
    val workingDirectory = Sgit.getRepoPath()
    if (optionSgitPath.isDefined) {
      assert(optionSgitPath.get == s"${workingDirectory.get}/.sgit")
    }

  }

  it should "return the path of the project folder" in {
    val workingDirectory = Sgit.getRepoPath()
    if (workingDirectory.isDefined) {
      assert(workingDirectory.get == s"${workingDirectory.get}")
    }

  }

}
