package ch.epfl.alcmp.net


import ch.epfl.alcmp.data.{InputType, SimulationResult, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import scala.collection.mutable

class SimulationListener(private val socket: Socket, private val typeId: TypeId) {

  type SimulationId = Int

  var ongoingSimulations: Map[SimulationId, SimulationResult] = Map.empty

  given BufferedReader =
    val inputStream = socket.getInputStream
    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))

  def handleOne(using reader: BufferedReader): Unit =
    val args = reader.readLine().split(Pattern.quote(" "), -1)
    val content = args(1)
    MessageId.valueOf(args(0)) match {
      case MessageId.REGISTER => handleRegister(Serdes.deserialize[RegisterMessage](content))
      case MessageId.DONE     => handleDone(Serdes.deserialize[DoneMessage](content))
      case MessageId.COMBINE  => handleCombine(Serdes.deserialize[CombineMessage](typeId, content))
      case MessageId.DIVIDE   => handleDivide(Serdes.deserialize[DivideMessage](typeId, content))
    }

  def run(): Unit =
    handleOne

  def handleRegister(message: RegisterMessage): Unit =
    ongoingSimulations += (message.id, new SimulationResult)
    handleOne

  def handleDone(message: DoneMessage): Unit =
    ongoingSimulations -= message.id
    if ongoingSimulations.isEmpty then finish else handleOne

  def handleDivide(message: DivideMessage): Unit = ???
  def handleCombine(message: CombineMessage): Unit = ???

  def finish: Unit = ???
}
