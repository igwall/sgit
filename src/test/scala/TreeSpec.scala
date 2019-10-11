import org.scalatest._
import app.components.Tree
import app.components.Sgit

class TreeSpec extends FlatSpec with Matchers {

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

    val sgitDirectory = Sgit.getSgitPath()
    if (sgitDirectory.isDefined) {
      val tree = Tree.createTree("origin", listOfPath, sgitDirectory.get)
      
    }

  }

}