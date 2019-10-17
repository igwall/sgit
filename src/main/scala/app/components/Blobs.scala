package app.components
import app.components.{Sgit, FileManager}
import java.io.File

object Blobs {
  //Path : String or Paths ?
  //Return the hash of the String

  def createBlob(
      fileToBlob: String,
      sgitDirectory: String
  ): Option[String] = {

    if (new File(fileToBlob).exists()) {
      val pathSplit = fileToBlob.split("/")
      val fileName = pathSplit(pathSplit.size - 1)
      val contentFile: String =
        s"$fileName\n${FileManager.extractContentFromPath(fileToBlob)}"
      val hashFileName: String = FileManager.createHash(contentFile)
      val blobDirectory = sgitDirectory + File.separator + "blobs"
      // Nomme le fichier avec le hash
      FileManager.createFile(hashFileName, contentFile, blobDirectory)
      // Copy le contenu dans le fichier
      //Retourne le hash
      return Some(hashFileName)
    } else {
      return None
    }

  }

  def extractName(sgitDirectory: String, blobHash: String): String = {
    val blob =
      FileManager.extractContentFromPath(sgitDirectory + "/blobs/" + blobHash)
    blob.split("\n")(0)
  }

  def getContent(hash: String, sgitDirectory: String): String = {
    val fullPath = s"$sgitDirectory/blobs/$hash"
    FileManager.extractContentFromPath(fullPath)
  }

}
