package ch.epfl.alcmp.simulation

import ch.epfl.alcmp.scripting.DCAssembler

import java.io.{BufferedReader, File, FileInputStream, FileOutputStream, FileReader, FileWriter, IOException, InputStreamReader}
import scala.io.Source

class Simulator {

  private val BIN_FOLDER = "src/main/bin/"
  private val PYTHON_FOLDER = "src/main/python/"

  def makeDCScript(base: String, div: String, comb: String): Unit =
    val script = DCAssembler.assemble(base, div, comb)
    val writer: FileWriter = new FileWriter(new File(BIN_FOLDER + "dc_script.py"))
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

  def runSimulator(): Unit =
    copyToBin("simulator.py")
    val process = ProcessBuilder("python", PYTHON_FOLDER + "simulator.py").start()
    val reader = new BufferedReader(new InputStreamReader(process.getInputStream))
    print(reader.readLine())
    process.destroy()

}
