package app.command
import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.math.max
import app.components.Stage
import app.components.Blobs
import app.components.FileManager

object Diff {
  // Main method for the command
  def getDiff(sgitDirectory: String, repoDirectory: String): String = {

    // Récupérer le stage
    val hashAndPath = Stage.getTuplesHashPath(sgitDirectory)
    hashAndPath.map { hashAndPath =>
      s"""\r${hashAndPath._2}  
          ${makeDiff(
        Blobs.getContent(hashAndPath._1, sgitDirectory).split("\n").toList,
        FileManager
          .extractContentFromShortenPath(hashAndPath._2, repoDirectory)
          .split("\n")
          .toList
      )}\n"""
    }.mkString
    // Récupérer le path pour dire qu'on fait le diff la dessus : /src/xxx.txt et avec les diffs en deesous
    // Séparer les blobs des paths
    // Pour chaque Blob et chaque path (Dans un map)
  }

  // Main part of the "diff" algorithm between two files
  def makeDiff(
      oldFile: List[String],
      newFile: List[String]
  ): String = {
    val newFileArray = newFile.toArray
    val oldFileArray = oldFile.toArray

    // newIndex for raws
    // oldIndex for cols
    @tailrec
    def populateMatrix(
        matrix: HashMap[(Int, Int), Int],
        oldFile: Array[String],
        newFile: Array[String],
        oldFileIndex: Int,
        newFileIndex: Int
    ): HashMap[(Int, Int), Int] = {
      // Si oldFileIndex et newFileIndex == 0
      if (oldFileIndex == 0 && newFileIndex == 0) {
        if (oldFile(oldFileIndex) == newFile(newFileIndex)) {
          val newMatrix = matrix + ((oldFileIndex, newFileIndex) -> 1)
          populateMatrix(
            newMatrix,
            oldFile,
            newFile,
            oldFileIndex + 1,
            newFileIndex
          )
        } else {
          val newMatrix = matrix + ((oldFileIndex, newFileIndex) -> 0)
          populateMatrix(
            newMatrix,
            oldFile,
            newFile,
            oldFileIndex + 1,
            newFileIndex
          )
        }
      }
      // Si oldIndex > oldFile.size -1 && newIndex < newFile.size -1
      else if (oldFileIndex > oldFile.size - 1 && newFileIndex < newFile.size - 1) {
        populateMatrix(matrix, oldFile, newFile, 0, newFileIndex + 1)
      } else if (oldFileIndex > oldFile.size - 1 && newFileIndex >= newFile.size - 1) {
        matrix
      } else {
        if (oldFile(oldFileIndex) == newFile(newFileIndex)) {
          // If the two lines are sames, the result is equal to upper left element + 1
          val newMatrix = matrix + ((oldFileIndex, newFileIndex) -> (matrix
            .get(
              oldFileIndex - 1,
              newFileIndex - 1
            )
            .getOrElse(0) + 1))
          populateMatrix(
            newMatrix,
            oldFile,
            newFile,
            oldFileIndex + 1,
            newFileIndex
          )
        } else {
          // Value is max of left element and upper element
          if (oldFileIndex == 0) {
            val newMatrix = matrix + ((oldFileIndex, newFileIndex) -> matrix
              .get(
                oldFileIndex,
                newFileIndex - 1
              )
              .getOrElse(0))

            populateMatrix(
              newMatrix,
              oldFile,
              newFile,
              oldFileIndex + 1,
              newFileIndex
            )

          } else if (newFileIndex == 0) {
            val newMatrix = matrix + ((oldFileIndex, newFileIndex) -> matrix
              .get(
                oldFileIndex - 1,
                newFileIndex
              )
              .getOrElse(0))

            populateMatrix(
              newMatrix,
              oldFile,
              newFile,
              oldFileIndex + 1,
              newFileIndex
            )
          } else {
            val newMatrix = matrix + ((oldFileIndex, newFileIndex) -> (max(
              matrix.get(oldFileIndex - 1, newFileIndex).getOrElse(0),
              matrix.get(oldFileIndex, newFileIndex - 1).getOrElse(0)
            )))
            populateMatrix(
              newMatrix,
              oldFile,
              newFile,
              oldFileIndex + 1,
              newFileIndex
            )
          }
        }
      }

    }
    val matrix = populateMatrix(new HashMap(), oldFileArray, newFileArray, 0, 0)
    val diffs = calcDiff(
      matrix,
      oldFileArray,
      newFileArray,
      oldFile.size - 1,
      newFile.size - 1
    )
    displayDiff(diffs)

  }

  def calcDiff(
      matrix: HashMap[(Int, Int), Int],
      oldFile: Array[String],
      newFile: Array[String],
      oldFileIndex: Int,
      newFileIndex: Int
  ): List[(String, String)] = {

    @tailrec
    def calc(
        matrix: HashMap[(Int, Int), Int],
        oldFile: Array[String],
        newFile: Array[String],
        oldFileIndex: Int,
        newFileIndex: Int,
        changes: List[(String, String)]
    ): List[(String, String)] = {
      val currentCase = matrix.get(oldFileIndex, newFileIndex).getOrElse(-1)
      val left = matrix.get(oldFileIndex - 1, newFileIndex).getOrElse(0)
      val upper = matrix.get(oldFileIndex, newFileIndex - 1).getOrElse(0)

      if (currentCase == -1) {
        //Soit dans le coin et j'ai fini upper == -1 et left = -1
        if (oldFileIndex == -1 && newFileIndex == -1) {
          changes
        } else if (newFileIndex == -1) {
          oldFile
            .dropRight(oldFile.size - 1 - oldFileIndex)
            .map(elem => ("--", elem))
            .toList ::: changes
        } else {
          // I'm exceding column
          newFile
            .dropRight(newFile.size - 1 - newFileIndex)
            .map(elem => ("++", elem))
            .toList ::: changes
        }
        // Soit mon lineIndex -1 => Faire que des moins
      } else {
        if (currentCase == upper) {
          //je fais un plus
          calc(
            matrix,
            oldFile,
            newFile,
            oldFileIndex,
            newFileIndex - 1,
            ("++", newFile(newFileIndex)) :: changes
          )
        } else if (currentCase == left) {
          calc(
            matrix,
            oldFile,
            newFile,
            oldFileIndex - 1,
            newFileIndex,
            ("--", oldFile(oldFileIndex)) :: changes
          )
        } else {
          calc(
            matrix,
            oldFile,
            newFile,
            oldFileIndex - 1,
            newFileIndex - 1,
            changes
          )
        }
      }
    }

    //if(i == 0)
    //else
    calc(
      matrix,
      oldFile,
      newFile,
      oldFile.size - 1,
      newFile.size - 1,
      List(("", ""))
    )
  }

  def displayDiff(values: List[(String, String)]): String = {
    val edited = values.filter(p => p._1 != "")
    edited.map { value =>
      if (value._1 == "++")
        s"\r${Console.GREEN}${value._1} ${value._2}${Console.RESET}\n"
      else s"\r${Console.RED}${value._1} ${value._2}${Console.RESET}\n"
    }.mkString
  }

}
