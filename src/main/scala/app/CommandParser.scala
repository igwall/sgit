package app.command
import app.command.Initializer
import app.command.AddCommand
import app.components.Sgit

/**
  * Class that parse arguments given in parameter and dispatch actions to the right behavior class.
  * Input: List of parameters (from main)
  * Output: Unit
  */
case class CommandParser(params: Array[String]) {

  def treatment() = params(0) match {
    case "init" =>
      val initializer = new Initializer()
      initializer.initialise
    case "add" =>
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      if (optionPath.isDefined && optionDirectory.isDefined) {
        val workingDirectory: String = optionPath.get
        val sgitDirectory: String = optionDirectory.get
        val add = new AddCommand(params(1), workingDirectory, sgitDirectory)
        val status = add.addToStage()
        if (status.isEmpty) {
          println("Error: Cannot add this file to repository")
        }
      } else {
        println("Error : You're not in sgit project.")
      }

    case "commit" =>
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      if (optionPath.isDefined && optionDirectory.isDefined) {
        val sgitDirectory: String = optionDirectory.get
        //If the user give a message
        if (params(1) == "-m") {
          Commit.create(sgitDirectory, params(2))
        } else {
          Commit.create(sgitDirectory, "")
        }

      } else {
        println("Error: It seems that your not in working directory")
      }
    case _ => println("Unknow command prompted...")

  }

}
