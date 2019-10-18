package app.components
import app.components.FileManager

case class Log(sgitDirectory: String, content: String) {

  def update(newContent: String): Log = {
    new Log(sgitDirectory, newContent + content)
  }

  def save() = {
    FileManager.delete(s"$sgitDirectory/log")
    FileManager.createFile("log", content, sgitDirectory)
  }

}
object Log {

  def apply(sgitDirectory: String): Log = {
    val logContent = FileManager.extractContentFromPath(s"$sgitDirectory/log")
    new Log(sgitDirectory, logContent)
  }

  def getContent(sgitDirectory: String): String = {
    FileManager.extractContentFromPath(s"$sgitDirectory/log")
  }
}
