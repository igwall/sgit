package app.command
import java.io.File
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Paths
import scala.annotation.tailrec

/**
  * /!\ I/O Class /!\
  * /!\ Side Effect Class /!\
  * Class that create all directories and files in .sgit
  */
class Initializer() {

  /**
    * initialise:
    * Method that return true if it could create a .sgit folder
    * Create a .sgit folder with all sub folders and files necessary to use sgit
    */
  def initialise: Boolean = {

    val path = Paths.get("").toAbsolutePath().toString()
    val folders = List("tags", "commits", "trees", "blobs", "branches", ".old")
    val files = List("STAGE", "HEAD")
    val sgitRepo = new File(".sgit")
    val sgitPath = path + File.separator + ".sgit"

    // TO DO : Check if we're not already in a sgit project
    if (!alreadyExistSgitFolder(path)) {
      sgitRepo.mkdir()
      folders.map(
        folder => new File(sgitPath + File.separator + folder).mkdir()
      )
      files.map(
        file => new File(sgitPath + File.separator + file).createNewFile()
      )
      val oldStage = new File(
        sgitPath + File.separator + ".old" + File.separator + "STAGE.old"
      ).createNewFile()
      println("Initialized empty Git repository in " + path)
      return true
    } else {
      println("Error: already existing directory: .sgit")
      return false
    }
  }

  def alreadyExistSgitFolder(path: String): Boolean = {
    @tailrec
    def search(directories: List[String], path: String): Boolean = {
      if (directories.isEmpty) {
        false
      } else {
        val currentPath = path + directories.head
        val currentDir = new File(currentPath + ".sgit")
        if (currentDir.exists) true
        else false || search(directories.tail, currentPath)
      }
    }
    return search(path.split(File.separator).toList.map(_ + "/"), "")
  }
}
