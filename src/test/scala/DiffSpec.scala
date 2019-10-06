package example
import org.scalatest._
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io._
import app.Diff
class DiffSpec extends FlatSpec with Matchers {



  "The diff command" should "print differences in this two files" in {
    val file1Path = Paths.get("file1.txt").toAbsolutePath()
    val file2Path = Paths.get("file2.txt").toAbsolutePath()
    //Write on file
    val writer1 = new PrintWriter(new File(file1Path.toString()))
    val writer2 = new PrintWriter(new File(file1Path.toString()))
    new PrintWriter(file1Path.toString()) { write("a\nb\nc\nd\ne\nf\ng"); close }
    new PrintWriter(file2Path.toString()) { write("a\nb\ng\nh\ni\ne\nf"); close }

    val diff = new Diff()
    diff.makeDiff(file1Path, file2Path)
    assert(true)
    
  } 
  
  it should "return true" in {
    val file1Path = Paths.get("file1.txt").toAbsolutePath()
    val file2Path = Paths.get("file2.txt").toAbsolutePath()
    //Write on file
    val writer1 = new PrintWriter(new File(file1Path.toString()))
    val writer2 = new PrintWriter(new File(file1Path.toString()))
    new PrintWriter(file1Path.toString()) { write("a\nb\nc\nd\ne\nf\ng"); close }
    new PrintWriter(file2Path.toString()) { write("a\nb\nc\nd\ne\nf\ng"); close }

    val diff = new Diff()
    assert(diff.thereDiff(file1Path, file2Path))
  }
}