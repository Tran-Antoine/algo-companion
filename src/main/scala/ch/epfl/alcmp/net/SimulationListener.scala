package ch.epfl.alcmp.net


import ch.epfl.alcmp.data.{InputType, SimulationData, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import scala.collection.mutable

class SimulationListener(private val socket: Socket, private val typeId: TypeId) {

  type SimulationId = Int

  private var ongoingSimulations: Map[SimulationId, SimulationData] = Map.empty

  given BufferedReader =
    val inputStream = socket.getInputStream
    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))

  def handleOne(using reader: BufferedReader): List[SimulationData] =
    val args = reader.readLine().split(Pattern.quote(" "), -1)
    val content = args(1)
    MessageId.valueOf(args(0)) match {
      case MessageId.REGISTER => handleRegister(Serdes.deserialize[RegisterMessage](content))
      case MessageId.DONE     => handleDone(Serdes.deserialize[DoneMessage](content))
      case MessageId.COMBINE  => handleCombine(Serdes.deserialize[CombineMessage](typeId, content))
      case MessageId.DIVIDE   => handleDivide(Serdes.deserialize[DivideMessage](typeId, content))
    }

  def run(): List[SimulationData] =
    handleOne

  def handleRegister(message: RegisterMessage): List[SimulationData] =
    ongoingSimulations += (message.id, new SimulationData)
    handleOne

  def handleDone(message: DoneMessage): List[SimulationData] =
    ongoingSimulations -= message.id
    if ongoingSimulations.isEmpty then finish else handleOne

  def handleDivide(message: DivideMessage): List[SimulationData] =
    ongoingSimulations(message.id).addDivisionData(message)
    handleOne

  def handleCombine(message: CombineMessage): List[SimulationData] =
    ongoingSimulations(message.id).addCombineData(message)
    handleOne

  def finish: List[SimulationData] = ongoingSimulations.toList.sorted((a, b) => a._1 - b._1).map(_._2)
}
