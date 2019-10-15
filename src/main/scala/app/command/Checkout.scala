package app.command
import app.command.{Commit,Diff}
object Checkout {

  //Détruire tous les fichiers (A NE SURTOUT PAS FAIRE COMME UN MONGOL EN TEST)
  // FAire la fonction de reconstruction réccursive à partir du commit
    def doCheckOut(sgitDirectory:String) : Option[String] = {
        val commitContent = Commit.extractContentLastCommit(sgitDirectory)
        if(Diff.)
    }
  

}
