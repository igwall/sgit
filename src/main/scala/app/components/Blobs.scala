package app.components
import app.components.Sgit
import java.io.File
import java.security.MessageDigest

object Blobs {
    //Path : String or Paths ?
    //Return the hash of the String 
    def createBlob(fileToBlob: String, projectFolder: String) : Option[String] = {
        //Récupère le path du project sgit, va dans le dossier blob
        //Génère un hash a partir du contenu du fichier
        if(new File(fileToBlob).exists()){
            val contentFile : String = FileManager.extractContentFromPath(fileToBlob)
            val hashFileName :String = createHash(contentFile)
            val blobDirectory  = projectFolder + File.separator + "blobs"
            // Nomme le fichier avec le hash
            FileManager.createFile(hashFileName, contentFile, blobDirectory )
            // Copy le contenu dans le fichier
            //Retourne le hash
            return Some(hashFileName)
            } else {
            return None
        }
        
        
    }

    def createHash(content : String) : String  = {
        MessageDigest
      .getInstance("SHA-1")
      .digest(content.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString

    }

}