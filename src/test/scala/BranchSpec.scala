import org.scalatest._
import app.components.{FileManager, Sgit}
import app.command.{Initializer}
import java.io.File
import app.components.Branch
class BranchSpec extends FlatSpec with Matchers {
  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.update("HEAD", "49d6567s", sgitDirectory)
    // Fait le test
    try test()
    finally {
      val repo = Sgit.getRepoPath().get
      if (new File(s"/.sgit").exists()) FileManager.delete(s"$repo/.sgit")
    }
  }

  it should ("create a branch") in {
    val sgitDirectory = Sgit.getSgitPath().get
    val res = Branch.createBranch("create-me", sgitDirectory)
    assert(res.get == "Branch correctly created")
  }
  it should "not create the branch" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val res = Branch.createBranch("create-me", sgitDirectory)
    assert(res.get == "Branch already existing")
  }
  it should "return None when HEAD is Empty" in {
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.delete(sgitDirectory + "/HEAD")
    FileManager.update("HEAD", "", sgitDirectory)
    val res = Branch.createBranch("create-me-please", sgitDirectory)
    assert(!res.isDefined)
  }
}
