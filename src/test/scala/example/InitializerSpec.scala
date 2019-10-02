package example
import java.io.File

import org.scalatest._
import app.Initializer

object InitializerSpec extends FlatSpec with Matchers {
  "The initializer" should "create a folder" in {
    val initializer = new Initializer()
    initializer.initialise
    val file = new File(".sgit")
    assert(new File(".sgit").exists())
  }
}
