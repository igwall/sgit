import org.scalatest._
import app.components.{FileManager, Sgit}
import app.command.{Initializer}
import java.io.File
import app.components.Branch
class BranchSpec extends FlatSpec with Matchers {
  // avant le test
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

  it should ("create a branch") in {
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.delete(sgitDirectory + "/HEAD")
    FileManager.createFile("HEAD", "uytezfze", sgitDirectory)
    val res = Branch.createBranch("create-me", sgitDirectory)
    assert(res.get == "Branch correctly created")
  }

  it should "not create the branch" in {
    val sgitDirectory = Sgit.getSgitPath().get
    Branch.createBranch("create-me", sgitDirectory)
    val res = Branch.createBranch("create-me", sgitDirectory)
    assert(res.get == "Branch already existing")
  }

  it should "return None when HEAD is Empty" in {
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.delete(sgitDirectory + "/HEAD")
    FileManager.createFile("HEAD", "", sgitDirectory)
    val res = Branch.createBranch("create-me-please", sgitDirectory)
    assert(!res.isDefined)
  }
}
