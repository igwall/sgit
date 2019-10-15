package app.command
import app.command.Initializer
import app.command.AddCommand
import app.components.Sgit
<<<<<<< HEAD
import app.components.Branch
=======
import app.command.Diff
>>>>>>> diff

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
          println(
            "${Console.RED}Error: Cannot add this file to repository${Console.RESET}"
          )
        }
      } else {
        println(
          s"${Console.RED}Error : You're not in sgit project.${Console.RESET}"
        )
      }

    case "commit" =>
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      if (optionPath.isDefined && optionDirectory.isDefined) {
        val sgitDirectory: String = optionDirectory.get
        val repoDirectory = optionPath.get
        //If the user give a message
        if (params.length == 3) {
          if (params(1) == "-m") {
            Commit.create(sgitDirectory, params(2), repoDirectory)
          }
        } else {
          Commit.create(sgitDirectory, " ", repoDirectory)
        }

      } else {
        println("Error: It seems that your not in sgit project")
      }

    case "status" =>
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      if (optionPath.isDefined && optionDirectory.isDefined) {
        val sgitDirectory: String = optionDirectory.get
        val repoDirectory: String = optionPath.get
        val message = Status.getStatus(sgitDirectory, repoDirectory)
        println(message)
      } else {
        println("Error: It seems that your not in sgit project")
      }
    case "diff" =>
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      if (optionPath.isDefined && optionDirectory.isDefined) {
        val sgitDirectory: String = optionDirectory.get
        val repoDirectory: String = optionPath.get
        val message = Diff.getDiff(sgitDirectory, repoDirectory)
        println(message)
      } else {
        println("Error: It seems that your not in working directory")
      }
    case "branch" =>
        val message = Branch.createBranch(params(1), sgitDirectory)
        if (message.isDefined) {
          println(message.get)
        } else {
          println("fatal: Not a valid object name: 'master'.")
        }
    case _ => println("Unknow command prompted...")

  }

}
