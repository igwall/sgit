import org.scalatest._
import app.components.{Tag, Head, FileManager, Sgit}
import app.command.{Initializer}
import java.io.File
class TagSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.update("HEAD", "49d6567s", sgitDirectory)
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

  it should ("create a tag") in {
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.delete(sgitDirectory + "/HEAD")
    FileManager.createFile("HEAD", "uytezfze", sgitDirectory)
    val head = Head(sgitDirectory)
    val res = Tag.createTag("new-tag", sgitDirectory, head)
    assert(res.get == "Tag correctly created")
  }

  it should "not create the tag" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val head: Head = Head(sgitDirectory)
    Tag.createTag("new-tag", sgitDirectory, head)
    val dontCreate = Tag.createTag("new-tag", sgitDirectory, head)
    assert(dontCreate.get == "Tag already existing")
  }

  it should "return None when HEAD is Empty" in {
    val sgitDirectory = Sgit.getSgitPath().get
    FileManager.delete(sgitDirectory + "/HEAD")
    FileManager.createFile("HEAD", "", sgitDirectory)
    val head: Head = Head(sgitDirectory)
    val res = Tag.createTag("create-me-please", sgitDirectory, head)
    assert(!res.isDefined)
  }

  it should "give all the tags" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val head: Head = Head(sgitDirectory)
    Tag.createTag("create-me-please", sgitDirectory, head)
    val res = Tag.getAllTag(sgitDirectory)
    assert(res.split("\n").size == 2)
  }

}
