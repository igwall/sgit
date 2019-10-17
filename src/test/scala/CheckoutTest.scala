import org.scalatest._
import app.command.Commit
import app.components.{Stage, Sgit, FileManager}

class CheckoutSpec extends FlatSpec with Matchers {

  "Checkout" should "check if there some diff in staged files before commit" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
  }
}
