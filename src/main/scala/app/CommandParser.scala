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

    val sgitManager = new Sgit()
    val optionPath : Option[String] = sgitManager.getPath()
    val path = optionPath.get
   

    def treatment ()  = params(0) match   {

        case "init" => {
            val initializer = new Initializer()
            initializer.initialise
        }
        case "add" => {
            val add = new AddCommand(params(1), path)
            add.addToStage()
        }
        case _ => println("Unknow command prompted...")
        
    }

}