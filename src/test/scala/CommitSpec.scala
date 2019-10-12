import org.scalatest._
import app.command.Commit
import app.components.{Stage, Sgit, FileManager}

class CommitSpec extends FlatSpec with Matchers {

  "Commit" should "a new file in commit with his own hash" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val hash = Commit.create(sgitDirectory, "message")
    assert(hash.isEmpty())
    assert(FileManager.exist(s"$sgitDirectory/trees/$hash"))
  }

  it should "replace the HEAD file with is new hash"

}
