package ch.epfl.alcmp.scripting

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.*
import matchers.*

import java.io.{BufferedReader, BufferedWriter, FileInputStream, FileOutputStream, FileReader, FileWriter, InputStream, InputStreamReader, OutputStreamWriter}
import java.net.ServerSocket
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

  "Assembly of simple maximum finder program" should "retrieve the correct output" in {

    val input = "5,8,3,7,10,23,16,2"
    val divide = "return arg[:n//2], arg[n//2:]"
    val combine = "return [arg0[0]] if arg0[0] > arg1[0] else [arg1[0]]"
    val base = "if n == 1: return arg"

    val script = DCAssembler.assemble(base, divide, combine)

    val server = ServerSocket(4000)

    val process = ProcessBuilder(
      "python", "src/test/python/template_assembly_helper.py", input).start()

    val sender = server.accept()
    val writer = new BufferedWriter(new OutputStreamWriter(sender.getOutputStream))
    writer.write(script)
    writer.flush()
    writer.close()

    val reader = new BufferedReader(new InputStreamReader(process.getInputStream))
    val output = reader.readLine()

    process.waitFor()
    process.destroy()
    server.close()

    output should be ("[23]")

  }

  private def read(name: String): String = Source.fromInputStream(new FileInputStream("src/test/resources/" + name)).mkString
}
