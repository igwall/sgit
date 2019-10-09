package app.command
import app.command.Initializer
import app.command.AddCommand
import app.components.Sgit

/**
 * Class that parse arguments given in parameter and dispatch actions to the right behavior class. 
 * Input: List of parameters (from main)
 * Output: Unit
 */
case class CommandParser (params : Array[String]) {


    def parse(): Unit = {
        val optionPath : Option[String] = Sgit.getRepoPath()
        val optionDirectory : Option[String] = Sgit.getSgitPath()
        if(optionPath.isDefined && optionDirectory.isDefined){
            val workingDirectory : String = optionPath.get
            val sgitDirectory: String = optionDirectory.get
            treatment(workingDirectory, sgitDirectory)
        } else {
            println("An error occured. You're not in a working directory.")
        }
    }




    def treatment (workingDirectory : String, sgitDirectory : String)  = params(0) match   {

        case "init" => {
            val initializer = new Initializer()
            initializer.initialise
        }
        case "add" => {
            val add = new AddCommand(params(1), workingDirectory,sgitDirectory)
            val status = add.addToStage()
            if(!status.isDefined){
                println("Error: Cannot add this file to repository")
            }
        }
        case _ => println("Unknow command prompted...")
        
    }

}