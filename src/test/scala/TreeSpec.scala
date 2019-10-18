import org.scalatest._
import app.components.{FileManager, Sgit, Tree}
import app.command.Initializer
import java.io.File

class TreeSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    // avant le test
    val init = new Initializer()
    init.initialise

    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get
    // Fait le test
    try test()
    finally {
      val repo = Sgit.getRepoPath()
      if (repo.isDefined) {
        if (new File("/.sgit").exists())
          FileManager.delete(s"${repo.get}/.sgit")
      }
    }
  }

  "Tree" should "create correct architecture" in {
    val pathListed1 = List("b", "fin")
    val pathListed2 = List("b", "c", "fin")
    val pathListed3 = List("e", "f", "fin")
    val pathListed4 = List("fin")
    val pathListed5 = List("fin")
    val listOfPath =
      List(pathListed1, pathListed2, pathListed3, pathListed4, pathListed5)
    val mapped = listOfPath.partition(_.length > 1)
    val trees = mapped._1.groupBy(elem => elem.head)
    val blobs = mapped._2
    val treeName = "origin"

    val sgitDirectory = Sgit.getSgitPath()
    if (sgitDirectory.isDefined) {
      val tree = Tree.createTree(treeName, listOfPath, sgitDirectory.get)
      val pathOfTree =
        s"${sgitDirectory.get}${File.separator}trees${File.separator}${tree.hash}"
      assert(FileManager.exist(pathOfTree))
    }
  }

}
