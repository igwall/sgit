package app

case class CommandParser (params : Array[String]) {

    def treatment ()  = params(0) match   {
        case "init" => println("Tu veux init en fait")
        case "add" => println("On va te mettre bien")
        case _ => println("Unknow command prompted...")
    }

}
