package app.components
import app.components.Sgit
import java.io.File


object Blobs {
    //Path : String or Paths ?
    //Return the hash of the String 
    def createBlob(fileToBlob: String, sgitDirectory: String) : Option[String] = {
        //Récupère le path du project sgit, va dans le dossier blob
        //Génère un hash a partir du contenu du fichier
        println(s"fileToBlob : $fileToBlob, existing : ${new File(fileToBlob).exists()}")
        if(new File(fileToBlob).exists()){
            val contentFile : String = FileManager.extractContentFromPath(fileToBlob)
            val hashFileName :String = FileManager.createHash(contentFile)
            val blobDirectory  = sgitDirectory + File.separator + "blobs"
            // Nomme le fichier avec le hash
            FileManager.createFile(hashFileName, contentFile, blobDirectory )
            // Copy le contenu dans le fichier
            //Retourne le hash
            return Some(hashFileName)
            } else {
            return None
        }
        
        
    }
}