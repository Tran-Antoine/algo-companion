package ch.epfl.alcmp.simulation

import ch.epfl.alcmp.data.{SimulationData, TypeId}
import ch.epfl.alcmp.data.TypeId.BinaryTreeType
import ch.epfl.alcmp.net.SimulationListener
import ch.epfl.alcmp.scripting.DCAssembler

import java.io.{BufferedReader, File, FileInputStream, FileOutputStream, FileReader, FileWriter, IOException, InputStreamReader}
import java.net.{ServerSocket, Socket}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

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
    type_id match
      case "ListType" => TypeId.ListType
      case "MatrixType" => TypeId.MatrixType
      case "BinaryTreeType" => TypeId.BinaryTreeType
      case "HeapType" => TypeId.HeapType
      case _ => throw new IllegalArgumentException


  //returns process that needs to be destroyed buy caller of function
  def runSimulator(base: String, div: String, comb: String,
                   typeId: String, dc_arg: String): Future[List[SimulationData]] =

    makeDCScript(base, div, comb)
    copyToBin("simulator.py")
    copyToBin("serde.py")
    val IdType = getType(typeId)
    if(IdType == TypeId.MatrixType) copyToBin("matrix.py")

    val server = ServerSocket(7777)
    val process = ProcessBuilder("python", BIN_FOLDER+"simulator.py", typeId, "7777", dc_arg).start()
    val socket = server.accept()

    val listener = new SimulationListener(socket, IdType)
    val task: Future[List[SimulationData]] = listener.run()

    //for testing python file
//    val reader = new BufferedReader(new InputStreamReader(process.getInputStream))
//    val output = reader.readLine()
//    println(output)

    task

}
