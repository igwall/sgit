import org.scalatest._
import app.components.{FileManager, Sgit, Tree}
import app.command.Initializer
import java.io.File

class TreeSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise

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

  "Tree" should "create correct architecture" in {

    val sgitDirectory: String = Sgit.getSgitPath().get

    val pathListed1 = List("b", "fin")
    val pathListed2 = List("b", "c", "fin")
    val pathListed3 = List("e", "f", "fin")
    val pathListed4 = List("fin")
    val pathListed5 = List("fin")
    val listOfPath =
      List(pathListed1, pathListed2, pathListed3, pathListed4, pathListed5)

    val treeName = "origin"

    val tree = Tree(treeName, listOfPath, sgitDirectory)
    tree.save()
    val pathOfTree =
      s"${sgitDirectory}${File.separator}trees${File.separator}${tree.hash}"
    assert(FileManager.exist(pathOfTree))
  }

}
