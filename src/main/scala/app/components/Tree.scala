package app.components
import java.io.File

case class Tree(
    name: String,
    trees: List[Tree],
    blobs: List[String],
    sgitRepository: String
) {

  val hash: String =
    FileManager.createHash(name + trees.mkString + blobs.mkString)

  // Reccursively save content
  def save() {
    val content =
      s"- ${name}\n- ${trees.mkString(",")}\n- ${blobs.mkString(",")}\n"
    val fileName = hash
    val path = s"${sgitRepository}/trees"
    FileManager.createFile(fileName, content, path)
    trees.map(tree => tree.save())
  }

}

object Tree {
  def apply(
      name: String,
      contentToSave: List[List[String]],
      sgitDirectory: String
  ): Tree = {
    createTree(name, contentToSave, sgitDirectory)
  }

  def createTree(
      fileName: String,
      paths: List[List[String]],
      sgitDirectory: String
  ): Tree = {
    val listOfPath = paths.partition(_.length > 1)
    val trees = listOfPath._1.groupBy[String](elem => elem.head)
    val blobs = listOfPath._2 // Get all the blobs DONE
    //Create sons of our current tree
    val subTrees = trees.map {
      case (name, tree) => {
        createTree(
          name,
          tree.map(_.tail),
          sgitDirectory
        )
      }
    }.toList
    // Get all the trees

    val tree = new Tree(
      fileName,
      subTrees,
      blobs.flatten,
      sgitDirectory
    )
    tree
  }
//TODO WORK ON PERSISTANT FILE OF TREE
}
