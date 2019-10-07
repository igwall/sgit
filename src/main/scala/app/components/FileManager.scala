package app.components
import java.io.File
import scala.io.Source

object FileManager {
    def searchFolder(file : File, folder : String) : Option[String] = {
        val fileToFind = new File(file.toString()+File.separator+folder).exists()
        println(file.toString()+File.separator+folder)
        if (file.exists()){
            return Some(file.toString()+File.separator+".sgit")
        } else {
            if (file.getParent() == null) {
                return None
            } else {
                searchFolder(file.getParentFile(), folder)
            }
        }
    }

    def extractContentFromPath(path: String) = {
        scala.io.Source.fromFile(path).mkString
    }

    def delete(path: String): Unit = {
        val file = new File(path)
        if (file.isDirectory()) {
          file.listFiles().foreach(f => delete(f.getCanonicalPath()))
        }
        file.delete()
      }
}