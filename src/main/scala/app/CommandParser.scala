package app

/**
 * Class that parse arguments given in parameter and dispatch actions to the right behavior class. 
 * Input: List of parameters (from main)
 * Output: Unit
 */
case class CommandParser (params : Array[String]) {

    def treatment ()  = params(0) match   {
        case "init" => {
            val initializer = new Initializer()
            initializer.initialise
        }
        case "add" => println("On va te mettre bien")
        case _ => println("Unknow command prompted...")
        
    }

}