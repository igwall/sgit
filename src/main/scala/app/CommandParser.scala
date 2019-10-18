package app.command
import app.command.{Initializer, AddCommand, Helper}
import app.components.{Sgit, Stage, Branch, Blobs}
import app.command.Diff
import java.io.File

/**
  * Class that parse arguments given in parameter and dispatch actions to the right behavior class.
  * Input: List of parameters (from main)
  * Output: Unit
  */
case class CommandParser(params: Array[String]) {

  def treatment() = params(0) match {
    case "init" =>
      val initializer = new Initializer()
      val res = initializer.initialise
      if (res.isDefined) {
        println(res.get)
      } else {
        println(
          s"${Console.RED_B}Error: already existing directory: .sgit${Console.RESET}"
        )
      }
    case "add" =>
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      if (optionPath.isDefined && optionDirectory.isDefined) {
        val workingDirectory: String = optionPath.get
        val sgitDirectory: String = optionDirectory.get
        if (new File(s"$workingDirectory/${params(1)}").exists) {
          val stage = Stage(sgitDirectory, workingDirectory)
          val blob = Blobs(sgitDirectory, workingDirectory, params(1))
          blob.save
          val add = AddCommand(sgitDirectory, workingDirectory)
          val newStage = add.addToStage(params(1), stage, blob)
          newStage.save()
        } else {
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
      val optionPath: Option[String] = Sgit.getRepoPath()
      val optionDirectory: Option[String] = Sgit.getSgitPath()
      val sgitDirectory: String = optionDirectory.get
      val repoDirectory: String = optionPath.get
      if (params.length == 2 && params(1) == "-av") {

        val message = Branch.getAllBranches(sgitDirectory)
        println(message)

      } else if (params.length == 2 && !params(1).contains("-")) {
        val message = Branch.createBranch(params(1), sgitDirectory)
        if (message.isDefined) {
          println(message.get)
        } else {
          println("fatal: Not a valid object name: 'master'.")
        }
      } else {
        println(
          "This command doesn't exist.. Type sgit --help for more informations"
        )
      }
    case _ =>
      println("Unknow command prompted...")
      println(Helper.getHelp)

  }

}
