package app.command
//import app.command.{Commit, Diff}
//import app.components.{Branch, Stage}
object Checkout {
  /*
  //Détruire tous les fichiers (A NE SURTOUT PAS FAIRE COMME UN MONGOL EN TEST)
  // FAire la fonction de reconstruction réccursive à partir du commit
  def doCheckOut(
      sgitDirectory: String,
      repoDirectory: String,
      branchName: String
  ): Option[String] = {
    //Check that there is no diff between Stage and files tracked.
    if (Diff.listOfDiff(sgitDirectory, repoDirectory).isEmpty) {
      Some("Some files are modified.. Can't checkout")
    } else {
      // Construct the futureStage in a file.
      val commitHash = Branch.getCommitFromBranch(sgitDirectory, sgitDirectory)
      println(commitHash)
      val newStage = Stage.createStageFromMainTree(sgitDirectory, commitHash)
      Some(newStage)
      // Once STAGE constructed, check that there is no files edited that are on future Stage
      //If there somes => Abort
      //Else
      // For each files in Stage => delete file.
      // in recursive way, check that if the folder is empty, destroy it too.
      // For each file in future stage, build it.
    }
    // Pour tous les fichiers du checkout vers qui on va (dev : foo.txt tracked)
    // Si j'ai un foo.txt non tracked annuler et dire de faire attention
    // +> Pour tous mes fihciers untracked, vérifier qu'ils ne sont pas dans le stage !

  }
 */
}
