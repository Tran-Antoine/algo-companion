package ch.epfl.alcmp.scripting

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.*
import matchers.*

import java.io.{FileInputStream, FileOutputStream, FileWriter, InputStream}
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

  "Assembly of user functions" should "be correct" in {

    val assembly = DCAssembler.assembleUserFunctions("return n == 1", "return arg[0:n//2], arg[n//2:n]", "return arg0 + arg1")
    val expected = read("assembly_example.txt")

    assembly should be (expected)
  }

  private def read(name: String): String = Source.fromInputStream(new FileInputStream("src/test/resources/" + name)).mkString
}
