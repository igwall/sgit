package app
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
    def initialise : Unit = {
        
        val path = Paths.get("").toAbsolutePath().toString()
        val folders = List("tags", "commits", "trees", "blobs", "branches")
        val files = List("STAGE","HEAD")
        val sgitRepo = new File(".sgit")
        val sgitPath = File.separator + ".sgit" + File.separator

        // TO DO : Check if we're not already in a sgit project
        if(!alreadyExistSgitFolder(path, File.separator)) {
            folders.map( folder => new File(sgitPath + folder).mkdir())
            files.map(file => new File(sgitPath + file).createNewFile())
            println("Success: sgit project correctly initialized ! ")
        } else {
            println("Fail: .sgit folder already existing... Abort")
        }
        
    }


    def alreadyExistSgitFolder(path: String, source: String):Boolean = {
        
        @tailrec
        def search(directories : List[String], path : String) : Boolean = {
            println("Current path : " + path)
            if (directories.isEmpty){
                false
            } else {
                val currentPath = path + File.separatorChar + directories.head + File.separatorChar +".sgit"
                val currentDir = new File(currentPath)
                if (currentDir.exists) true else  false || search(directories.tail, currentPath)
            }
        }
        return search(path.split(File.separator).toList, File.separator)
    }
}


