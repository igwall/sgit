import java.io.File

import app.command.{AddCommand, Initializer}
import org.scalatest._
import app.components.{FileManager, Sgit}

class SgitSpec extends FlatSpec with Matchers {

  "The sgit.getSgitPath()" should "return the path of the .sgit of the project" in {
    val optionSgitPath: Option[String] = Sgit.getSgitPath()
    val workingDirectory = Sgit.getRepoPath()
    if (optionSgitPath.isDefined) {
      assert(optionSgitPath.get == s"${workingDirectory.get}/.sgit")
    }

  }

}
