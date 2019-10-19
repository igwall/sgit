import org.scalatest._
import java.io.File
import app.components.{Sgit, Stage, Blobs, FileManager}
import app.command.{Initializer, AddCommand}

class StageSpec extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {
    val initializer = new Initializer()
    initializer.initialise
    try test()
    finally {
      val repo = Sgit.getRepoPath().get
      if (new File(s"$repo/.sgit").exists()) FileManager.delete(s"$repo/.sgit")
      if (!new File(s"$repo/src/test/testEnvironment/devandMaster.txt")
            .exists())
        FileManager.createFile(
          "/src/test/testEnvironment/devandMaster.txt",
          "i'm on dev and master",
          repo
        )
    }
  }

  "Stage" should "add line into itself" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    val blob = Blobs(
      sgitDirectory,
      workingDirectory,
      "/src/test/testEnvironment/file1.txt"
    )
    val res = blob.save()
    if (res.isDefined) {
      val stage: Stage = Stage(sgitDirectory, workingDirectory)
      val newStage = stage.addElement(
        blob.hash,
        "/src/test/testEnvironment/file1.txt"
      )
      newStage.save()
      assert(
        newStage.content.contains(
          s"${blob.hash} ${stage.separator} /src/test/testEnvironment/file1.txt"
        )
      )
    }
  }

  it should "edit the hash if the file is edited then added" in {
    //We edit the file1.txt to check that we could update the hash
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    val stage: Stage = Stage(sgitDirectory, workingDirectory)
    val newStage = stage.addElement(
      "aaaa",
      s"${workingDirectory}/src/test/testEnvironment/file1.txt"
    )

    val finalStage = newStage.addElement(
      "bbbb",
      s"${workingDirectory}/src/test/testEnvironment/file1.txt"
    )
    assert(
      finalStage.content.contains(
        s"bbbb ${stage.separator} /src/test/testEnvironment/file1.txt"
      )
    )
  }

  it should "clean the path" in {

    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    val stage: Stage = Stage(sgitDirectory, workingDirectory)
    val path = s"${workingDirectory}/5/6/monfichier.txt"

    val cleanedPath = stage.cleanPath(path)
    assert(cleanedPath == "/5/6/monfichier.txt")
  }

  it should "get all the paths" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    val stage: Stage = Stage(sgitDirectory, workingDirectory)
    val newStage = stage.addElement(
      "aaaa",
      "/src/test/testEnvironment/file1.txt"
    )

    val finalStage = newStage.addElement(
      "bbbb",
      "/src/test/testEnvironment/file2.txt"
    )
    val paths = finalStage.getAllPath()
    assert(paths.size == 2)
  }

  it should "return a list of blob" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get
    val stage: Stage = Stage(sgitDirectory, workingDirectory)
    val newStage = stage.addElement(
      "aaaa",
      s"${workingDirectory}/src/test/testEnvironment/file1.txt"
    )
    val blobs = newStage.getAllBlobs()
    assert(blobs.size == 1)
  }

  it should "return correct tuple hash and path" in {
    val sgitDirectory = Sgit.getSgitPath().get
    val workingDirectory = Sgit.getRepoPath().get

    val specialPath = s"$workingDirectory/src/test/testEnvironment"
    FileManager.update("newFile.txt", "i'm content", specialPath)

    val fileToAdd: String = "/src/test/testEnvironment/newFile.txt"
    val blob = Blobs(sgitDirectory, workingDirectory, fileToAdd)
    blob.save()
    val stage = Stage(sgitDirectory, workingDirectory)
    val add = AddCommand(sgitDirectory, workingDirectory)
    val newStage = add.addToStage(fileToAdd, stage, blob)
    newStage.save()
    val finalStage = Stage(sgitDirectory, workingDirectory)
    val res = finalStage.getTuplesHashPath()
    assert(res.head._1 == blob.hash)
    assert(res.head._2 == fileToAdd)
  }
  /*

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
    val fileToAdd: String = "/src/test/testEnvironment/devandMaster.txt"
    val add = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    add.addToStage()
    val commitCreated =
      Commit.create(sgitDirectory, "populate master", repoDirectory)
    val branchCreated = Branch.createBranch("dev", sgitDirectory)
    if (branchCreated.isDefined) {
      val devCOmmit = Branch.getCommitFromBranch(sgitDirectory, "dev")
      println(commitCreated.get == devCOmmit)
    }

    val fileToAdd2: String = "/src/test/testEnvironment/masteronlyfile.txt"
    val add2 = new AddCommand(fileToAdd, repoDirectory, sgitDirectory)
    val res = add2.addToStage()
    println(s"second add : $res")
    println(s"> Stage before second commit: ${Stage.getContent(sgitDirectory)}")
    Commit.create(sgitDirectory, "populate master again", repoDirectory)
    println(s"> Stage after second commit: ${Stage.getContent(sgitDirectory)}")

    Stage.deleteFilesInStage(sgitDirectory, repoDirectory)
    Branch.setCurrentBranch(sgitDirectory, "dev")

    val commitHash = Branch.getCommitFromBranch(sgitDirectory, "dev")
    val mainTree = Commit.getTree(sgitDirectory, commitHash)
    val contentFromhash = Stage.extractFromHash(sgitDirectory, mainTree)
    val newStage = Stage.createStageFromMainTree(sgitDirectory, mainTree)
    Stage.importStage(sgitDirectory, newStage)
  }
 */

}
