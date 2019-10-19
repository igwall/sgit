import org.scalatest._
import app.command.Commit
import java.io.File
import app.command.{Initializer}
import app.components.{Stage, Sgit, FileManager, Head, Log}

class CommitSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise

    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    val stage = Stage(sgitDirectory, workingDirectory)
    val newStage =
      stage.addElement("monhash", "monpath")
    newStage.save()
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
    val workingDirectory = Sgit.getRepoPath().get
    val head: Head = Head(sgitDirectory)
    val stage: Stage = Stage(sgitDirectory, workingDirectory)
    val log = Log(sgitDirectory)

    val commit =
      Commit(
        sgitDirectory,
        workingDirectory,
        stage,
        log,
        head,
        "my wonderfull message"
      )
    val res = commit.save
    assert(res.isDefined)
  }
}
