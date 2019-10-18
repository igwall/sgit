import org.scalatest._
import app.command.{Diff, Initializer}
import app.components.{Sgit, FileManager}
import java.io.File

class DiffSpec extends FlatSpec with Matchers {

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

  it should "create the matrix of content" in {
    val file1 = List("a", "b", "c", "d", "e")
    val file2 = List("a", "c", "f", "e")
    val diff = Diff.makeDiff(file1, file2)
    assert(!diff.isEmpty())
  }

  it should "get the diff from stage and current working tree" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
    println(Diff.getDiff(sgitDirectory, repoDirectory).mkString)
  }

  it should "return an empty list if there no diff" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
    assert(Diff.getDiff(sgitDirectory, repoDirectory).isEmpty())

  }
}
