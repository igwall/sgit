import org.scalatest._
import app.command.Commit
import java.io.File
import app.command.{Initializer}
import app.components.{Stage, Sgit, FileManager}

class CommitSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise

    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get

    Stage.addElement(
      "monhashtestcommit",
      "/test/test/test/",
      sgitDirectory,
      repoDirectory
    )
    // Fait le test
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

  "Commit" should "add new file in commit with his own hash" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
    val res = Commit.create(sgitDirectory, "message", repoDirectory)
    assert(res.isDefined)
  }
}
