import org.scalatest._
import app.command.Commit
import app.components.{Stage, Sgit, FileManager}

class CommitSpec extends FlatSpec with Matchers {

  "Commit" should "add new file in commit with his own hash" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val hash = Commit.create(sgitDirectory, "message")
    assert(!hash.isEmpty())
  }
}
