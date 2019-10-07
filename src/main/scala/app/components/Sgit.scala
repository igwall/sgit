package app.components
import app.components.FileManager
import java.io.File


// Faire un singleton ? => Eviter de faire plusieurs calculs de path inutiles => Limiter I/O
class Sgit(){
    //Return the path of the .sgit project
    def getPath() : Option[String] =  {
        val path : File = new File(".")
        val canonical  = path.getCanonicalFile()
        return FileManager.searchFolder(canonical, "./sgit")
    }
}