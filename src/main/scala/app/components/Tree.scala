package app.components

case class Tree(val name: String = "", trees : List[String], blobs : List[String] ) {

}

object Tree {
  def createTree(fileName : String, paths : List[List[String]], sgitDirectory : String): Tree = {
    val listOfPath = paths.partition(_.length == 1)
    val trees = listOfPath._1.groupBy[String](elem => elem.head)
    val blobs = listOfPath._2 // Get all the blobs DONE
    //Create sons of our current tree
    val subTrees = trees.map{
      case (name, tree) => {
        createTree(
          "" + name,
          tree.map(_.tail),
          sgitDirectory
        )
      }
    }
    // Get all the trees
    new Tree(fileName, subTrees,blobs.flatten)
  }

//TODO
}