import app.command.AddCommand
import org.scalatest._
import app.components.Sgit

class SgitSpec extends FlatSpec with Matchers {


    "The sgit.getSgitPath()" should "return the path of the .sgit of the project" in {
        val optionSgitPath : Option[String] = Sgit.getSgitPath()
        if(optionSgitPath.isDefined){
            assert(optionSgitPath.get == "/Users/lucasgoncalves/Git/sgit/.sgit" )
        }

    }

}  