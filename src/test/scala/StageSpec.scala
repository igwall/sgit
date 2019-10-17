import org.scalatest._
import app.components.Stage
import java.io.File
import app.components.FileManager
import app.components.Sgit
import app.command.{Initializer, Commit, AddCommand}
import app.components.{Blobs, Branch, Head}

class StageSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    val initializer = new Initializer()
    initializer.initialise
    try test()
    finally {
      val repo = Sgit.getRepoPath().get
      if (new File(s"$repo/.sgit").exists()) FileManager.delete(s"$repo/.sgit")
    }
  }

  "Stage" should "add line into itself" in {
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get
    val blob = Blobs.createBlob(
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder
    )
    if (blob.isDefined) {
      Stage.addElement(
        blob.get,
        s"${repoFolder}/src/test/testEnvironment/file1.txt",
        sgitFolder,
        repoFolder
      )
      val stage = new File(sgitFolder + "PATH")
      assert(
        !FileManager.extractContentFromPath(sgitFolder + "/STAGE").isEmpty()
      )
    }
  }
  it should "edit the hash if the file is edited then added" in {
    //We edit the file1.txt to check that we could update the hash
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get

    Stage.addElement(
      "aaaa",
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder,
      repoFolder
    )

    Stage.addElement(
      "bbbb",
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder,
      repoFolder
    )
    val res = FileManager
      .extractContentFromPath(s"${sgitFolder}${File.separator}STAGE")
      .split("\n")
      .size == 1
    assert(
      res
    )
  }

  it should "clean the path" in {
    val repoFolder = Sgit.getRepoPath().get
    val path = s"${repoFolder}/5/6/monfichier.txt"
    val cleanedPath = Stage.cleanPath(path, repoFolder)
    assert(cleanedPath == "/5/6/monfichier.txt")
  }

  it should "get all the paths" in {
    val sgitFolder = Sgit.getSgitPath().get
    val repoFolder = Sgit.getRepoPath().get
    Stage.addElement(
      "bbbb",
      s"${repoFolder}/src/test/testEnvironment/file1.txt",
      sgitFolder,
      repoFolder
    )
    Stage.addElement(
      "aaaa",
      s"${repoFolder}/src/test/testEnvironment/file2.txt",
      sgitFolder,
      repoFolder
    )
    val paths = Stage.getAllPath(sgitFolder)
    assert(paths.size == 2)
  }

  it should "return a list of blob" in {
    val sgitFolder = Sgit.getSgitPath().get
    val blobs = Stage.getAllBlobs(sgitFolder)
  }

  it should "construct a stage file" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get

    // populate branch dev
    val fileToAdd: String = "/src/test/testEnvironment/file1.txt"
    val add = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    add.addToStage()
    val commitCreated =
      Commit.create(sgitDirectory, "populate master", repoDirectory)
    val branchCreated = Branch.createBranch("dev", sgitDirectory)

    Branch.setCurrentBranch(sgitDirectory, "dev")

    val commitHash = Branch.getCommitFromBranch(sgitDirectory, "dev")
    val mainTree = Commit.getTree(sgitDirectory, commitHash)
    val contentFromhash = Stage.extractFromHash(sgitDirectory, mainTree)
    val newStage = Stage.createStageFromMainTree(sgitDirectory, mainTree)
    assert(newStage.split("\n").size == 1)
  }

  it should "reconstruct the tree from a path given in parameter" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val repoDirectory = Sgit.getRepoPath().get

    // populate branch dev
    val fileToAdd: String = "/src/test/testEnvironment/file1.txt"
    val add = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    add.addToStage()
    val commitCreated =
      Commit.create(sgitDirectory, "populate master", repoDirectory)
    val branchCreated = Branch.createBranch("dev", sgitDirectory)

    FileManager.update(
      "/src/test/testEnvironment/file1.txt",
      "igwall is not there",
      sgitDirectory
    )
    val add2 = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    add2.addToStage()
    Commit.create(sgitDirectory, "populate master again", repoDirectory)

    Branch.setCurrentBranch(sgitDirectory, "dev")

    val commitHash = Branch.getCommitFromBranch(sgitDirectory, "dev")
    val mainTree = Commit.getTree(sgitDirectory, commitHash)
    val contentFromhash = Stage.extractFromHash(sgitDirectory, mainTree)
    val newStage = Stage.createStageFromMainTree(sgitDirectory, mainTree)
    println(Stage.importStage(sgitDirectory, newStage))

  }

}
