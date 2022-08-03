package ch.epfl.alcmp.scripting

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.*
import matchers.*

import java.io.{BufferedReader, BufferedWriter, FileInputStream, FileOutputStream, FileWriter, InputStream, InputStreamReader}
import scala.io.Source

class DCAssemblerTest extends AnyFlatSpec with should.Matchers {

  "Reading content" should "return null string iff path is invalid" in {
    DCAssembler.readContent("foo.py") should be (null)
    DCAssembler.readTemplate should not be (null)
  }

  "Adding line to function" should "indent correctly" in {
    val insertion = DCAssembler.insertCode("def print2(n):", "print(n)")
    val expected  =  read("indent_example.txt")

    insertion should be (expected)
  }

  "Assembly of simple program" should "retrieve the correct output" ignore {

    val input = "(5,8,3,7,10,23,16,2)"
    val divide = "return arg[:n//2], arg[n//2:]"
    val combine = "[arg0[0]] if arg0[0] > arg1[0] else [arg1[1]]"
    val base = "if n == 1: return arg0"

    val script = DCAssembler.assemble(base, divide, combine)

    val process = ProcessBuilder("py", "src/test/python/dc_template_test.py", input, script).start()
    val reader = new BufferedReader(new InputStreamReader(process.getInputStream))

    val output = reader.readLine()

    process.waitFor()
    process.destroy()

    output should be ("23")

  }

  private def read(name: String): String = Source.fromInputStream(new FileInputStream("src/test/resources/" + name)).mkString
}
