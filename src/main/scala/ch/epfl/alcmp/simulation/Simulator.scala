package ch.epfl.alcmp.simulation

import ch.epfl.alcmp.scripting.DCAssembler

import java.io.{File, FileInputStream, FileOutputStream, FileReader, FileWriter, IOException}
import scala.io.Source

class Simulator {

  private val BIN_FOLDER = "src/main/bin/"
  private val PYTHON_FOLDER = "src/main/python/"

  def makeDCScript(base: String, div: String, comb: String): Unit =
    val script = DCAssembler.assemble(base, div, comb)
    val writer: FileWriter = new FileWriter(new File(BIN_FOLDER + "DC_script.py"))
    writer.write(script)
    writer.close()

  def copyToBin(fileName: String): Unit =
    val src = PYTHON_FOLDER + fileName
    val dest = BIN_FOLDER + fileName
    val inputChannel = new FileInputStream(src).getChannel
    val outputChannel = new FileOutputStream(dest).getChannel
    outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
    inputChannel.close()
    outputChannel.close()


}
