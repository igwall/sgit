package app.components
import app.components.FileManager

object Log {
  def update(sgitDirectory: String, content: String) = {
    val oldContent = getContent(sgitDirectory)
    FileManager.update("log", content + oldContent, sgitDirectory)
  }

  def getContent(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(s"$sgitDirectory/log")
  }
}
