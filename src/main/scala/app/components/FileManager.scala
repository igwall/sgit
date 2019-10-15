package app.components
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import scala.io.Source
import java.security.MessageDigest

object FileManager {
  def searchFolder(file: File, folder: String): Option[File] = {
    val newFileToFind = new File(file.toString() + File.separator + folder)
    if (newFileToFind.exists()) {
      Some(newFileToFind)
    } else {
      if (file.getParent() == null) {
        return None
      } else {
        searchFolder(file.getParentFile(), folder)
      }
    }
  }

  def update(fileName: String, content: String, path: String) = {
    if (exist(path)) {
      delete(fileName)
      createFile(fileName, content, path)
    } else {
      createFile(fileName, content, path)
    }
  }

  def extractContentFromPath(path: String) = {
    val reader = Source.fromFile(path)
    val content = reader.mkString
    reader.close()
    content
  }

  def extractContentFromShortenPath(path: String, repoDirectory: String) = {
    val reader = Source.fromFile(repoDirectory + path)
    val content = reader.mkString
    reader.close()
    content
  }

  def createFile(name: String, data: String, path: String): Unit = {
    val file = new File(path + File.separator + name)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(data)
    bw.close()
  }

  def delete(path: String): Unit = {
    val file = new File(path)
    if (file.isDirectory()) {
      file.listFiles().foreach(f => delete(f.getCanonicalPath()))
    }
    file.delete()
  }

  def createHash(content: String): String = {
    MessageDigest
      .getInstance("SHA-1")
      .digest(content.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString
  }

  def exist(path: String): Boolean = {
    return new File(path).exists
  }

  def cleanPath(path: String, pathToSubstain: String): String = {
    path drop pathToSubstain.size
  }

  def isDirectory(path: String): Boolean = {
    val isDirectory = new File(path).isDirectory
    isDirectory
  }

  def getAllFilesFromPath(
      workingDirectory: String,
      origin: String
  ): List[String] = {
    // We get all the files and directory from repo
    val allFiles = new File(workingDirectory).listFiles().toList
    // WE need to separate files and folder

    val sortedFilesFolders = allFiles.partition(file => !file.isDirectory())
    // Add all the files to a list of path
    // WARNING -!!!! Si j'ai pas de fichiers
    val pathOfFiles = sortedFilesFolders._1.map(
      file =>
        cleanPath(file.getCanonicalPath(), origin) // Clean all the path of files according working directory :)
    )

    val pathOfSubFolders = sortedFilesFolders._2
    if (pathOfSubFolders.isEmpty) {
      pathOfFiles.map(path => cleanPath(path, origin))
      pathOfFiles.filter(!_.contains("/.sgit/"))
    } else {
      val subFiles = pathOfSubFolders.map(
        folder => getAllFilesFromPath(folder.getPath(), origin)
      )
      // return the list of files in this folder and all the path from subdirectory files
      pathOfFiles ::: subFiles.flatten.filter(!_.contains("/.sgit/"))
    }

    // Reccursively get all the files from subfolders
  }

}
