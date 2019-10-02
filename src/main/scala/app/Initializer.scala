package app
import java.io.File
import java.nio.file.FileAlreadyExistsException

/**
 * /!\ I/O Class /!\
 * /!\ Side Effect Class /!\
 * Class that create all directories and files in .sgit
*/

class Initializer() {
    def initialise : Unit = {
        val path = new File(".").getAbsolutePath
        val folders = List("tags", "commits", "trees", "blobs", "branches")
        val files = List("STAGE","HEAD")
        val sgitRepo = new File(".sgit")
        val sgitPath = path + "/.sgit/"
        
// TO DO : Check if we're not already in a sgit project

        if(sgitRepo.mkdir()) {
            folders.map( folder => new File(sgitPath + folder).mkdir())
            files.map(file => new File(sgitPath + file).createNewFile())
            println("Success: sgit project correctly initialized ! ")
        } else {
            println("Fail: .sgit folder already existing... Abort")
        }
        
    }
}
