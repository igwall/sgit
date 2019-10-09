package app.components
import app.components.FileManager
import java.io.File


object Sgit{
    //Return the path of the .sgit project
    def getRepoPath() : Option[String] =  {
        val path : File = new File(".")
        val canonical  = path.getCanonicalFile()
        val returnedFolder =  FileManager.searchFolder(canonical, ".sgit/")
        if(returnedFolder.isDefined){
            val mayParentFolder = returnedFolder.get.getParentFile
            if(mayParentFolder.exists()){
                return Some(mayParentFolder.toString)
            } else {
                None
            }
        } else {
            None
        }
    }

    def getSgitPath() : Option[String] = {
        val path : File = new File(".")
        val canonical  = path.getCanonicalFile()
        val folderPath = FileManager.searchFolder(canonical, ".sgit/")
        if(folderPath.isDefined){
            return Some(folderPath.get.toString)
        } else {
            return None
        }
    }
}