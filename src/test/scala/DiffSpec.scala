import org.scalatest._
import app.command.Diff
import app.components.Sgit

class DiffSpec extends FlatSpec with Matchers {
  it should "create the matrix of content" in {
    val file1 = List("a", "b", "c", "d", "e")
    val file2 = List("a", "c", "f", "e")
    val diff = Diff.makeDiff(file1, file2)
  }

  it should "get the diff from stage and current working tree" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
    println(Diff.getDiff(sgitDirectory, repoDirectory).mkString)
  }
}
