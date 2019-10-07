package app
import app.command.CommandParser

//Launcher of the App, it's our entry point.  
object  Launcher extends App {
  val parser : CommandParser = new CommandParser(args);
  parser.treatment()
}
