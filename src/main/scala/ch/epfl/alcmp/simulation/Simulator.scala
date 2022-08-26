package ch.epfl.alcmp.simulation

import ch.epfl.alcmp.data.{SimulationData, TypeId}
import ch.epfl.alcmp.data.TypeId.BinaryTreeType
import ch.epfl.alcmp.net.SimulationListener
import ch.epfl.alcmp.scripting.DCAssembler

import java.io.{BufferedReader, File, FileInputStream, FileOutputStream, FileReader, FileWriter, IOException, InputStreamReader}
import java.net.{ServerSocket, Socket}
import scala.concurrent.Future
import scala.io.Source

class Simulator {

  private val BIN_FOLDER = "src/main/bin/"
  private val PYTHON_FOLDER = "src/main/python/"

  private def makeDCScript(base: String, div: String, comb: String): Unit =
    val script = DCAssembler.assemble(base, div, comb)
    val writer: FileWriter = new FileWriter(new File(BIN_FOLDER + "dc_script.py"))
    writer.write(script)
    writer.close()

  private def copyToBin(file: String): Unit =
    val src = PYTHON_FOLDER+file
    val dest = BIN_FOLDER+file
    val inputChannel = new FileInputStream(src).getChannel
    val outputChannel = new FileOutputStream(dest).getChannel
    outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
    inputChannel.close()
    outputChannel.close()

  private def getType(type_id: String): TypeId =
    if type_id == "ListType"       then return TypeId.ListType
    if type_id == "MatrixType"     then return TypeId.MatrixType
    if type_id == "BinaryTreeType" then return TypeId.BinaryTreeType
    if type_id == "HeapType"       then return TypeId.HeapType
    else throw new IllegalArgumentException

  
  def runSimulator(base: String, div: String, comb: String,
                   typeId: String, port: String, dc_arg: String): Process =
    makeDCScript(base, div, comb)
    copyToBin("simulator.py")
    copyToBin("serde.py")
    copyToBin("matrix.py") //TODO how to know when to copy this

    val server = ServerSocket(port.toInt)
    val listener = new SimulationListener(new Socket("localhost", port.toInt), getType(typeId))
    val sender = server.accept()
    val task: Future[List[SimulationData]] = listener.run()
    print(task)

    val process = ProcessBuilder("python", PYTHON_FOLDER+"simulator.py", typeId, port, dc_arg).start()
    process

}
