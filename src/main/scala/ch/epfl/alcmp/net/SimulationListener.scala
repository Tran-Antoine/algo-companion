package ch.epfl.alcmp.net


import ch.epfl.alcmp.data.{InputType, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class SimulationListener(private val socket: Socket, private val typeId: TypeId) {

  def run(): Unit =

    val inputStream = socket.getInputStream
    val reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))

    def handleOne(): Unit =
      val args = reader.readLine().split(Pattern.quote(" "), -1)
      val content = args(1)
      MessageId.valueOf(args(0)) match {
        case MessageId.REGISTER => handleRegister(Serdes.deserialize[RegisterMessage](content))
        case MessageId.DONE     => handleDone(Serdes.deserialize[DoneMessage](content))
        case MessageId.COMBINE  => handleCombine(Serdes.deserialize[CombineMessage](typeId, content))
        case MessageId.DIVIDE   => handleDivide(Serdes.deserialize[DivideMessage](typeId, content))
      }

  def handleRegister(message: RegisterMessage): Unit = ???
  def handleDone(message: DoneMessage): Unit = ???
  def handleDivide(message: DivideMessage): Unit = ???
  def handleCombine(message: CombineMessage): Unit = ???
}
