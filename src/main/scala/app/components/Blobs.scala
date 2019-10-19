package app.components
import app.components.{FileManager}
import java.security.MessageDigest

case class Blobs(
    sgitDirectory: String,
    content: String,
    filePath: String
) {

  val hash = createHash(content + filePath)
  val fileName = filePath.split("/").toList.last
  def save(): Option[String] = {

    val blobDirectory = s"$sgitDirectory/blobs"
    // Nomme le fichier avec le hash
    val res = FileManager.createFile(
      hash,
      s"$fileName\n$content",
      blobDirectory
    )
    // Copy le contenu dans le fichier
    //Retourne le hash
    if (res.isDefined) {
      return Some(hash)
    } else {
      None
    }
  }

  def createHash(content: String): String = {
    MessageDigest
      .getInstance("SHA-1")
      .digest(content.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString
  }

}
object Blobs {

  def apply(
      sgitDirectory: String,
      workingDirectory: String,
      filePath: String
  ): Blobs = {
    if (filePath.contains(workingDirectory)) {
      val content =
        FileManager.extractContentFromPath(s"$filePath")
      new Blobs(sgitDirectory, content, filePath)
    } else {
      val content =
        FileManager.extractContentFromPath(s"$workingDirectory/$filePath")
      new Blobs(sgitDirectory, content, filePath)
    }

  }

  def getFromHash(sgitDirectory: String, blobHash: String): Blobs = {
    val export =
      FileManager.extractContentFromPath(sgitDirectory + "/blobs/" + blobHash)
    val name = export.split("\n")(0)
    val content = export.split("\n").toList.tail.mkString
    new Blobs(sgitDirectory, content, name)
  }

}
