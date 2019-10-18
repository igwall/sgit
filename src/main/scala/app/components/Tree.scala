package app.components
import java.io.File

case class Tree(
    name: String,
    trees: List[String],
    blobs: List[String],
    sgitRepository: String
) {

  def save() {
    val content =
      s"- ${name}\n- ${trees.mkString(",")}\n- ${blobs.mkString(",")}\n"
    val fileName = hash
    val path = s"${sgitRepository}/trees"
    FileManager.createFile(fileName, content, path)
  }

  val hash: String =
    FileManager.createHash(name + trees.mkString + blobs.mkString)

}

object Tree {
  def apply(
      name: String,
      contentToSave: List[List[String]],
      sgitDirectory: String
  )  : Tree =  {



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
      subTrees.map(elem => elem.hash),
      blobs.flatten,
      sgitDirectory
    )
    tree.save()
    tree
  }
//TODO WORK ON PERSISTANT FILE OF TREE
}
