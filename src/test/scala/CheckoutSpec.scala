import org.scalatest._
import app.command.{Commit, Initializer, AddCommand, Checkout}
import app.components.{Stage, Sgit, FileManager, Branch}
import java.io.File

class CheckoutSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get

    // populate branch dev
    Branch.createBranch("dev", sgitDirectory)
    Branch.setCurrentBranch(sgitDirectory, "dev")
    val fileToAdd: String = "/src/test/testEnvironment/file1.txt"
    val add = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    Commit.create(sgitDirectory, "populate dev", repoDirectory)

    //populate branch master
    Branch.setCurrentBranch(sgitDirectory, "master")
    FileManager.update(
      "file1.txt",
      "new content",
      s"$repoDirectory/src/test/testEnvironment"
    )
    val add3 = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    Commit.create(sgitDirectory, "commit for master", repoDirectory)

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

  "Checkout" should "check if there some diff in staged files before commit" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
    val res = Checkout.doCheckOut(sgitDirectory, repoDirectory, "dev")
    if (res.isDefined) {
      println(res.get)
    }

  }
}
