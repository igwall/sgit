import org.scalatest._
import app.command.Diff

class DiffSpec extends FlatSpec with Matchers {
  it should "create the matrix of content" in {
    val file1 = List("a", "b", "c", "d", "e")
    val file2 = List("a", "c", "f", "e")
    val diff = Diff.makeDiff(file1, file2)
    print(diff)
  }
}
